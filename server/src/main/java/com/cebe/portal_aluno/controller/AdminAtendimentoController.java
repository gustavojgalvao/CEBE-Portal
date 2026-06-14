package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.response.AtendimentoResponseDTO;
import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.entity.MensagemAtendimento;
import com.cebe.portal_aluno.entity.enums.StatusAtendimento;
import com.cebe.portal_aluno.repository.AtendimentoRepository;
import com.cebe.portal_aluno.repository.MensagemAtendimentoRepository;
import com.cebe.portal_aluno.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/atendimentos")
@CrossOrigin("*")
public class AdminAtendimentoController {

    private final AtendimentoRepository atendimentoRepository;
    private final MensagemAtendimentoRepository mensagemRepository;
    private final SseService sseService;

    public AdminAtendimentoController(AtendimentoRepository atendimentoRepository,
                                       MensagemAtendimentoRepository mensagemRepository,
                                       SseService sseService) {
        this.atendimentoRepository = atendimentoRepository;
        this.mensagemRepository = mensagemRepository;
        this.sseService = sseService;
    }

    // converte a entidade para DTO para não expor dados sensíveis do aluno (como a senha)
    private AtendimentoResponseDTO toDTO(Atendimento a) {
        return new AtendimentoResponseDTO(
            a.getId(),
            a.getAluno() != null ? a.getAluno().getId() : null,
            a.getAluno() != null ? a.getAluno().getNome() : "Desconhecido",
            a.getStatusAtendimento() != null ? a.getStatusAtendimento().name() : null,
            a.getMensagem(),
            a.getDataHora()
        );
    }

    @GetMapping
    public ResponseEntity<List<AtendimentoResponseDTO>> listarTodos() {
        List<AtendimentoResponseDTO> lista = atendimentoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return atendimentoRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/mensagens")
    public ResponseEntity<List<MensagemAtendimento>> listarMensagens(@PathVariable Integer id) {
        return ResponseEntity.ok(mensagemRepository.findByAtendimentoIdOrderByDataHoraAsc(id));
    }

    // conexão SSE para a secretaria receber mensagens em tempo real
    @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter abrirStream(@PathVariable Integer id) {
        return sseService.subscribe(id);
    }

    @PostMapping("/{id}/mensagens")
    public ResponseEntity<MensagemAtendimento> enviarMensagem(
            @PathVariable Integer id,
            @RequestBody MensagemRequest request) {

        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        // se o chamado ainda estava pendente, muda para em andamento
        if (atendimento.getStatusAtendimento() == StatusAtendimento.Pendente) {
            atendimento.setStatusAtendimento(StatusAtendimento.Em_andamento);
            atendimentoRepository.save(atendimento);
        }

        MensagemAtendimento msg = MensagemAtendimento.builder()
                .atendimento(atendimento)
                .remetenteTipo("ADMIN")
                .mensagem(request.texto())
                .dataHora(LocalDateTime.now())
                .build();

        MensagemAtendimento salva = mensagemRepository.save(msg);
        sseService.notifySubscribers(id, salva);

        return ResponseEntity.ok(salva);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AtendimentoResponseDTO> atualizarStatus(
            @PathVariable Integer id,
            @RequestBody StatusRequest request) {

        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        // aceita "Em andamento" (com espaço) ou "Em_andamento" (com underscore)
        String status = request.status().replace(" ", "_");
        try {
            atendimento.setStatusAtendimento(StatusAtendimento.valueOf(status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(toDTO(atendimentoRepository.save(atendimento)));
    }

    public record MensagemRequest(String texto) {}
    public record StatusRequest(String status) {}
}

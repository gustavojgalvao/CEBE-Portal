package com.cebe.portal_aluno.controller;

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

    public AdminAtendimentoController(AtendimentoRepository atendimentoRepository, MensagemAtendimentoRepository mensagemRepository, SseService sseService) {
        this.atendimentoRepository = atendimentoRepository;
        this.mensagemRepository = mensagemRepository;
        this.sseService = sseService;
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listarTodos() {
        return ResponseEntity.ok(atendimentoRepository.findAll());
    }

    @GetMapping("/{id}/mensagens")
    public ResponseEntity<List<MensagemAtendimento>> listarMensagens(@PathVariable Integer id) {
        return ResponseEntity.ok(mensagemRepository.findByAtendimentoIdOrderByDataHoraAsc(id));
    }

    @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMensagens(@PathVariable Integer id) {
        // Admin also subscribes to the same Atendimento channel
        return sseService.subscribe(id);
    }

    @PostMapping("/{id}/mensagens")
    public ResponseEntity<MensagemAtendimento> enviarMensagem(
            @PathVariable Integer id,
            @RequestBody MensagemRequest request) {
        
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
                
        // Atualizar o status do atendimento se a atendente responder
        if (atendimento.getStatusAtendimento() == StatusAtendimento.Pendente) {
            atendimento.setStatusAtendimento(StatusAtendimento.valueOf("Em andamento"));
            atendimentoRepository.save(atendimento);
        }

        MensagemAtendimento msg = MensagemAtendimento.builder()
                .atendimento(atendimento)
                .remetenteTipo("ADMIN")
                .mensagem(request.texto())
                .dataHora(LocalDateTime.now())
                .build();
                
        MensagemAtendimento savedMsg = mensagemRepository.save(msg);
        sseService.notifySubscribers(id, savedMsg);
        
        return ResponseEntity.ok(savedMsg);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Atendimento> atualizarStatus(
            @PathVariable Integer id,
            @RequestBody StatusRequest request) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
        atendimento.setStatusAtendimento(StatusAtendimento.valueOf(request.status()));
        return ResponseEntity.ok(atendimentoRepository.save(atendimento));
    }

    public record MensagemRequest(String texto) {}
    public record StatusRequest(String status) {}
}

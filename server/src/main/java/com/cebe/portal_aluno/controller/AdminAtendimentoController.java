package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.entity.MensagemAtendimento;
import com.cebe.portal_aluno.entity.enums.StatusAtendimento;
import com.cebe.portal_aluno.repository.AtendimentoRepository;
import com.cebe.portal_aluno.repository.MensagemAtendimentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/atendimentos")
@CrossOrigin("*")
public class AdminAtendimentoController {

    private final AtendimentoRepository atendimentoRepository;
    private final MensagemAtendimentoRepository mensagemRepository;

    public AdminAtendimentoController(AtendimentoRepository atendimentoRepository, MensagemAtendimentoRepository mensagemRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.mensagemRepository = mensagemRepository;
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listarTodos() {
        return ResponseEntity.ok(atendimentoRepository.findAll());
    }

    @GetMapping("/{id}/mensagens")
    public ResponseEntity<List<MensagemAtendimento>> listarMensagens(@PathVariable Integer id) {
        return ResponseEntity.ok(mensagemRepository.findByAtendimentoIdOrderByDataHoraAsc(id));
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
                
        return ResponseEntity.ok(mensagemRepository.save(msg));
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

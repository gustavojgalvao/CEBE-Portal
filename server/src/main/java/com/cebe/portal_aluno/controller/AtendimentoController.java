package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.entity.MensagemAtendimento;
import com.cebe.portal_aluno.repository.MensagemAtendimentoRepository;
import com.cebe.portal_aluno.service.AtendimentoService;
import com.cebe.portal_aluno.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/atendimentos")
@CrossOrigin("*")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;
    private final MensagemAtendimentoRepository mensagemRepository;
    private final SseService sseService;

    public AtendimentoController(AtendimentoService atendimentoService, MensagemAtendimentoRepository mensagemRepository, SseService sseService) {
        this.atendimentoService = atendimentoService;
        this.mensagemRepository = mensagemRepository;
        this.sseService = sseService;
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listarTodos() {
        return ResponseEntity.ok(atendimentoService.listarTodos());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Atendimento>> buscarPorAluno(@AuthenticationPrincipal Aluno aluno) {
        return ResponseEntity.ok(atendimentoService.buscarPorAluno(aluno));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Atendimento>> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atendimentoService.buscarPorId(id));
    }

    // O aluno logado abre um chamado — o idAluno vem do token, não do body
    @PostMapping
    public ResponseEntity<Atendimento> abrirChamado(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Aluno aluno) {

        String mensagem = body.getOrDefault("mensagem", "Sem descrição");
        String statusStr = body.getOrDefault("statusAtendimento", "Pendente");

        Atendimento atendimento = atendimentoService.abrirChamado(aluno, mensagem, statusStr);
        return ResponseEntity.ok(atendimento);
    }

    @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMensagens(
            @PathVariable Integer id,
            @AuthenticationPrincipal Aluno aluno) {
        
        Atendimento atendimento = atendimentoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
                
        // Verifica se o chamado pertence ao aluno logado
        if (!atendimento.getAluno().getId().equals(aluno.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        
        return sseService.subscribe(id);
    }

    @GetMapping("/{id}/mensagens")
    public ResponseEntity<List<MensagemAtendimento>> listarMensagens(
            @PathVariable Integer id,
            @AuthenticationPrincipal Aluno aluno) {
        
        Atendimento atendimento = atendimentoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
                
        // Verifica se o chamado pertence ao aluno logado
        if (!atendimento.getAluno().getId().equals(aluno.getId())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(mensagemRepository.findByAtendimentoIdOrderByDataHoraAsc(id));
    }

    @PostMapping("/{id}/mensagens")
    public ResponseEntity<MensagemAtendimento> enviarMensagem(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Aluno aluno) {
            
        Atendimento atendimento = atendimentoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));
                
        // Verifica se o chamado pertence ao aluno logado
        if (!atendimento.getAluno().getId().equals(aluno.getId())) {
            return ResponseEntity.status(403).build();
        }
        
        String texto = body.getOrDefault("texto", "");
        if (texto.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        MensagemAtendimento msg = MensagemAtendimento.builder()
                .atendimento(atendimento)
                .remetenteTipo("ALUNO")
                .mensagem(texto)
                .dataHora(LocalDateTime.now())
                .build();
                
        MensagemAtendimento savedMsg = mensagemRepository.save(msg);
        sseService.notifySubscribers(id, savedMsg);
        
        return ResponseEntity.ok(savedMsg);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atendimento> atualizar(
            @PathVariable Integer id,
            @RequestBody Atendimento atendimento) {
        atendimento.setId(id);
        return ResponseEntity.ok(atendimentoService.salvar(atendimento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        atendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


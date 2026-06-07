package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Notificacao;
import com.cebe.portal_aluno.repository.NotificacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin("*")
public class NotificacaoController {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoController(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Notificacao>> buscarPorAluno(@AuthenticationPrincipal Aluno aluno) {
        return ResponseEntity.ok(notificacaoRepository.findByAlunoIdOrderByDataHoraDesc(aluno.getId()));
    }

    @PutMapping("/{id}/ler")
    public ResponseEntity<Void> marcarComoLida(
            @PathVariable Integer id,
            @AuthenticationPrincipal Aluno aluno) {
        
        Optional<Notificacao> notifOpt = notificacaoRepository.findById(id);
        if (notifOpt.isPresent()) {
            Notificacao notif = notifOpt.get();
            // Verifica se pertence ao aluno
            if (notif.getAluno().getId().equals(aluno.getId())) {
                notif.setLida(true);
                notificacaoRepository.save(notif);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/ler-todas")
    public ResponseEntity<Void> marcarTodasComoLida(@AuthenticationPrincipal Aluno aluno) {
        List<Notificacao> notificacoes = notificacaoRepository.findByAlunoIdOrderByDataHoraDesc(aluno.getId());
        for (Notificacao notif : notificacoes) {
            notif.setLida(true);
        }
        notificacaoRepository.saveAll(notificacoes);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id,
            @AuthenticationPrincipal Aluno aluno) {

        Optional<Notificacao> notifOpt = notificacaoRepository.findById(id);
        if (notifOpt.isPresent()) {
            Notificacao notif = notifOpt.get();
            // Garante que o aluno só pode deletar suas próprias notificações
            if (notif.getAluno().getId().equals(aluno.getId())) {
                notificacaoRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}

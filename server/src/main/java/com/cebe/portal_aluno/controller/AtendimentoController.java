package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.service.AtendimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/atendimentos")
@CrossOrigin("*")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listarTodos() {
        return ResponseEntity.ok(atendimentoService.listarTodos());
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


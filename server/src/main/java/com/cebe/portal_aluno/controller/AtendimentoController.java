package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.service.AtendimentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<Atendimento> listarTodos() {
        return atendimentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Atendimento> buscarPorId(@PathVariable Integer id) {
        return atendimentoService.buscarPorId(id);
    }

    @PostMapping
    public Atendimento salvar(@RequestBody Atendimento atendimento) {
        return atendimentoService.salvar(atendimento);
    }

    @PutMapping("/{id}")
    public Atendimento atualizar(
            @PathVariable Integer id,
            @RequestBody Atendimento atendimento
    ) {
        atendimento.setId(id);
        return atendimentoService.salvar(atendimento);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        atendimentoService.deletar(id);
    }
}

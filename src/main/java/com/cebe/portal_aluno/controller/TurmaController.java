package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Turma;
import com.cebe.portal_aluno.service.TurmaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
@CrossOrigin("*")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @GetMapping
    public List<Turma> listarTodos() {
        return turmaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Turma> buscarPorId(@PathVariable Integer id) {
        return turmaService.buscarPorId(id);
    }

    @PostMapping
    public Turma salvar(@RequestBody Turma turma) {
        return turmaService.salvar(turma);
    }

    @PutMapping("/{id}")
    public Turma atualizar(
            @PathVariable Integer id,
            @RequestBody Turma turma
    ) {
        turma.setId(id);
        return turmaService.salvar(turma);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        turmaService.deletar(id);
    }
}

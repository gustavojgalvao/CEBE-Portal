package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Matricula;
import com.cebe.portal_aluno.service.MatriculaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matriculas")
@CrossOrigin("*")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public List<Matricula> listarTodos() {
        return matriculaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Matricula> buscarPorId(@PathVariable Integer id) {
        return matriculaService.buscarPorId(id);
    }

    @PostMapping
    public Matricula salvar(@RequestBody Matricula matricula) {
        return matriculaService.salvar(matricula);
    }

    @PutMapping("/{id}")
    public Matricula atualizar(
            @PathVariable Integer id,
            @RequestBody Matricula matricula
    ) {
        matricula.setId(id);
        return matriculaService.salvar(matricula);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        matriculaService.deletar(id);
    }
}

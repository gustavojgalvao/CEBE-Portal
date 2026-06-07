package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.MatriculaRequestDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Matricula;
import com.cebe.portal_aluno.service.MatriculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<Matricula>> listarTodos() {
        return ResponseEntity.ok(matriculaService.listarTodos());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Matricula>> minhasMatriculas(@AuthenticationPrincipal Aluno aluno) {
        return ResponseEntity.ok(matriculaService.buscarPorAluno(aluno));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Matricula>> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(matriculaService.buscarPorId(id));
    }

    // Recebe { idAluno, idTurma } e cria a matrícula
    @PostMapping
    public ResponseEntity<Matricula> salvar(@RequestBody MatriculaRequestDTO dto) {
        return ResponseEntity.ok(matriculaService.criarMatricula(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matricula> atualizar(
            @PathVariable Integer id,
            @RequestBody Matricula matricula) {
        matricula.setId(id);
        return ResponseEntity.ok(matriculaService.salvar(matricula));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        matriculaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.ProfessorRequestDTO;
import com.cebe.portal_aluno.dto.response.ProfessorResponseDTO;
import com.cebe.portal_aluno.entity.Professor;
import com.cebe.portal_aluno.service.ProfessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professores")
@CrossOrigin("*")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<Professor> listarTodos() {
        return professorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Professor> buscarPorId(@PathVariable Integer id) {
        return professorService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criar(
            @RequestBody ProfessorRequestDTO dto
    ){
        return ResponseEntity.ok(professorService.criar(dto));
    }

    @PutMapping("/{id}")
    public Professor atualizar(
            @PathVariable Integer id,
            @RequestBody Professor professor
    ) {
        professor.setId(id);
        return professorService.criar(professor);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        professorService.deletar(id);
    }
}

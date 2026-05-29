package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.ProfessorRequestDTO;
import com.cebe.portal_aluno.dto.response.ProfessorResponseDTO;
import com.cebe.portal_aluno.service.ProfessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
@CrossOrigin("*")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listar() {
        return ResponseEntity.ok(professorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(professorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criar(
            @RequestBody ProfessorRequestDTO dto
    ){
        return ResponseEntity.ok(professorService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody ProfessorRequestDTO dto
    ) {
        return ResponseEntity.ok(professorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

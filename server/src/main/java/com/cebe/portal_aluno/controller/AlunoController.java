package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.AlunoRequestDTO;
import com.cebe.portal_aluno.dto.response.AlunoResponseDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@CrossOrigin("*")
public class AlunoController {

    @Autowired
    private AlunoService service;

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(
            @RequestBody AlunoRequestDTO dto) {

        return ResponseEntity.ok(service.criarAluno(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listar() {

        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody AlunoRequestDTO dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    // Quando o aluno chama GET /alunos/me com o token dele,
    // este método retorna os dados do próprio aluno pra identificar no front-end.
    @GetMapping("/me")
    public ResponseEntity<AlunoResponseDTO> meuPerfil(
            @AuthenticationPrincipal Aluno aluno) {
        return ResponseEntity.ok(service.buscarPorId(aluno.getId()));
    }
}

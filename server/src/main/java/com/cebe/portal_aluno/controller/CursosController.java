package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.entity.Cursos;
import com.cebe.portal_aluno.service.CursosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
@CrossOrigin("*")
public class CursosController {

    private final CursosService cursosService;

    public CursosController(CursosService cursosService) {
        this.cursosService = cursosService;
    }

    @GetMapping
    public List<Cursos> listarTodos() {
        return cursosService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Cursos> buscarPorId(@PathVariable Integer id) {
        return cursosService.buscarPorId(id);
    }

    @PostMapping
    public Cursos salvar(@RequestBody Cursos cursos) {
        return cursosService.salvar(cursos);
    }

    @PutMapping("/{id}")
    public Cursos atualizar(
            @PathVariable Integer id,
            @RequestBody Cursos cursos
    ) {
        cursos.setId(id);
        return cursosService.salvar(cursos);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        cursosService.deletar(id);
    }
}

package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.Cursos;
import com.cebe.portal_aluno.repository.CursosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursosService {

    private final CursosRepository cursosRepository;

    public CursosService(CursosRepository cursosRepository) {
        this.cursosRepository = cursosRepository;
    }

    public List<Cursos> listarTodos() {
        return cursosRepository.findAll();
    }

    public Optional<Cursos> buscarPorId(Integer id) {
        return cursosRepository.findById(id);
    }

    public Cursos salvar(Cursos cursos) {
        return cursosRepository.save(cursos);
    }

    public void deletar(Integer id) {
        cursosRepository.deleteById(id);
    }
}
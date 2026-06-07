package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository
        extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByAlunoId(Integer alunoId);
}

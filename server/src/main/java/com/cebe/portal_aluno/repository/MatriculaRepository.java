package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository
        extends JpaRepository<Matricula, Integer> {
}

package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository
        extends JpaRepository<Turma, Integer> {
}

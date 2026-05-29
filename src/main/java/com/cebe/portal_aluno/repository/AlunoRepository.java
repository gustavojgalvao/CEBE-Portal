package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository
        extends JpaRepository<Aluno, Integer> {
}
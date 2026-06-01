package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository
        extends JpaRepository<Professor, Integer> {
}
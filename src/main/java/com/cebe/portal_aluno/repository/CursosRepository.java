package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursosRepository
        extends JpaRepository<Cursos, Integer> {
}

package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository
        extends JpaRepository<Aluno, Integer> {
    Optional<Aluno> findByEmail(String email);
}
package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository
        extends JpaRepository<Atendimento, Integer> {
}

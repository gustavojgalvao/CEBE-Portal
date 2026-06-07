package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtendimentoRepository
        extends JpaRepository<Atendimento, Integer> {
    List<Atendimento> findByAlunoId(Integer alunoId);
}

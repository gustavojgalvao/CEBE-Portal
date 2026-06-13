package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.MensagemAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemAtendimentoRepository extends JpaRepository<MensagemAtendimento, Integer> {
    List<MensagemAtendimento> findByAtendimentoIdOrderByDataHoraAsc(Integer atendimentoId);
}

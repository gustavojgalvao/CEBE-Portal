package com.cebe.portal_aluno.repository;

import com.cebe.portal_aluno.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByAlunoIdOrderByDataHoraDesc(Integer alunoId);
}

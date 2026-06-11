package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Notificacao;
import com.cebe.portal_aluno.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Cria e persiste uma notificação para um aluno.
     *
     * @param aluno    destinatário
     * @param mensagem texto da notificação
     * @param tipo     categoria (ex: MATRICULA, FINANCEIRO, ATENDIMENTO, AVISO)
     */
    public Notificacao criar(Aluno aluno, String mensagem, String tipo) {
        Notificacao notif = new Notificacao();
        notif.setAluno(aluno);
        notif.setMensagem(mensagem);
        notif.setTipo(tipo);
        notif.setLida(false);
        notif.setDataHora(LocalDateTime.now());
        return notificacaoRepository.save(notif);
    }
}

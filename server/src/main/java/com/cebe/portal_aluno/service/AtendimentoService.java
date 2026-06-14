package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.entity.enums.StatusAtendimento;
import com.cebe.portal_aluno.repository.AtendimentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final NotificacaoService notificacaoService;
    private final com.cebe.portal_aluno.repository.MensagemAtendimentoRepository mensagemRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              NotificacaoService notificacaoService,
                              com.cebe.portal_aluno.repository.MensagemAtendimentoRepository mensagemRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.notificacaoService = notificacaoService;
        this.mensagemRepository = mensagemRepository;
    }

    public List<Atendimento> listarTodos() {
        return atendimentoRepository.findAll();
    }

    public Optional<Atendimento> buscarPorId(Integer id) {
        return atendimentoRepository.findById(id);
    }

    public List<Atendimento> buscarPorAluno(Aluno aluno) {
        return atendimentoRepository.findByAlunoId(aluno.getId());
    }

    public Atendimento salvar(Atendimento atendimento) {
        return atendimentoRepository.save(atendimento);
    }

    // Cria um chamado associado ao aluno logado
    public Atendimento abrirChamado(Aluno aluno, String mensagem, String statusStr) {
        StatusAtendimento status;
        try {
            status = StatusAtendimento.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            status = StatusAtendimento.Pendente;
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setAluno(aluno);
        atendimento.setMensagem(mensagem);
        atendimento.setStatusAtendimento(status);
        atendimento.setDataHora(LocalDateTime.now());

        Atendimento salvo = atendimentoRepository.save(atendimento);

        // Cria notificação automática de chamado aberto
        notificacaoService.criar(aluno,
                "Seu chamado #" + salvo.getId() + " foi aberto com sucesso. Aguarde o retorno da secretaria.",
                "ATENDIMENTO");

        return salvo;
    }

    @org.springframework.transaction.annotation.Transactional
    public void deletar(Integer id) {
        mensagemRepository.deleteByAtendimentoId(id);
        atendimentoRepository.deleteById(id);
    }
}


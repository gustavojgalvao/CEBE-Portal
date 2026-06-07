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

    public AtendimentoService(AtendimentoRepository atendimentoRepository) {
        this.atendimentoRepository = atendimentoRepository;
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

        return atendimentoRepository.save(atendimento);
    }

    public void deletar(Integer id) {
        atendimentoRepository.deleteById(id);
    }
}


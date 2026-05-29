package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.entity.Atendimento;
import com.cebe.portal_aluno.repository.AtendimentoRepository;
import org.springframework.stereotype.Service;

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

    public Atendimento salvar(Atendimento atendimento) {
        return atendimentoRepository.save(atendimento);
    }

    public void deletar(Integer id) {
        atendimentoRepository.deleteById(id);
    }
}

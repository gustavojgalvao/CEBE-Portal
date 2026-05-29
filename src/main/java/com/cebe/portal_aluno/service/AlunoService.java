package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.dto.request.AlunoRequestDTO;
import com.cebe.portal_aluno.dto.response.AlunoResponseDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.repository.AlunoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    public AlunoResponseDTO criarAluno(AlunoRequestDTO dto) {

        Aluno aluno = new Aluno();

        aluno.setNome(dto.getNome());
        aluno.setTelefone(dto.getTelefone());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setSenha(dto.getSenha());

        Aluno salvo = repository.save(aluno);

        return converterParaDTO(salvo);
    }

    public List<AlunoResponseDTO> listar() {

        List<Aluno> alunos = repository.findAll();

        return alunos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public AlunoResponseDTO buscarPorId(Integer id) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        return converterParaDTO(aluno);
    }

    public AlunoResponseDTO atualizar(
            Integer id,
            AlunoRequestDTO dto) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        aluno.setNome(dto.getNome());
        aluno.setTelefone(dto.getTelefone());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setSenha(dto.getSenha());

        Aluno atualizado = repository.save(aluno);

        return converterParaDTO(atualizado);
    }

    public void deletar(Integer id) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        repository.delete(aluno);
    }

    private AlunoResponseDTO converterParaDTO(Aluno aluno) {

        AlunoResponseDTO dto = new AlunoResponseDTO();

        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setTelefone(aluno.getTelefone());
        dto.setCpf(aluno.getCpf());
        dto.setEmail(aluno.getEmail());

        return dto;
    }
}
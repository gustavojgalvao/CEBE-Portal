package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.dto.request.AlunoRequestDTO;
import com.cebe.portal_aluno.dto.response.AlunoResponseDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.exception.RecursoNaoEncontradoException;
import com.cebe.portal_aluno.repository.AlunoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AlunoResponseDTO criarAluno(AlunoRequestDTO dto) {

        Aluno aluno = new Aluno();

        aluno.setNome(dto.nome());
        aluno.setTelefone(dto.telefone());
        aluno.setCpf(dto.cpf());
        aluno.setEmail(dto.email());
        aluno.setSenha(passwordEncoder.encode(dto.dataNascimento())); // aniversário vira senha

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
                        new RecursoNaoEncontradoException("Aluno não encontrado com o ID: " + id));

        return converterParaDTO(aluno);
    }

    public AlunoResponseDTO atualizar(
            Integer id,
            AlunoRequestDTO dto) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado com o ID: " + id));

        aluno.setNome(dto.nome());
        aluno.setTelefone(dto.telefone());
        aluno.setCpf(dto.cpf());
        aluno.setEmail(dto.email());
        aluno.setSenha(passwordEncoder.encode(dto.dataNascimento())); // aniversário vira senha

        Aluno atualizado = repository.save(aluno);

        return converterParaDTO(atualizado);
    }

    public void deletar(Integer id) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Aluno não encontrado com o ID: " + id));

        repository.delete(aluno);
    }

    private AlunoResponseDTO converterParaDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getTelefone(),
                aluno.getCpf(),
                aluno.getEmail()
        );
    }
}
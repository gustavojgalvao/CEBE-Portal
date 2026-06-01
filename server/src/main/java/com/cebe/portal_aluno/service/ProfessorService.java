package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.dto.request.ProfessorRequestDTO;
import com.cebe.portal_aluno.dto.response.ProfessorResponseDTO;
import com.cebe.portal_aluno.entity.Professor;
import com.cebe.portal_aluno.exception.RecursoNaoEncontradoException;
import com.cebe.portal_aluno.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public ProfessorResponseDTO criar(ProfessorRequestDTO dto){

        Professor professor = new Professor();

        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setEspecializacao(dto.especializacao());

        Professor professorSalvo = professorRepository.save(professor);

        return converterParaDTO(professorSalvo);
    }

    public List<ProfessorResponseDTO> listar(){

        return professorRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public ProfessorResponseDTO buscarPorId(Integer id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com o ID: " + id));
        return converterParaDTO(professor);
    }

    public ProfessorResponseDTO atualizar(Integer id, ProfessorRequestDTO dto) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com o ID: " + id));

        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setEspecializacao(dto.especializacao());

        Professor atualizado = professorRepository.save(professor);
        return converterParaDTO(atualizado);
    }

    public void deletar(Integer id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com o ID: " + id));
        professorRepository.delete(professor);
    }

    private ProfessorResponseDTO converterParaDTO(Professor professor) {
        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getEspecializacao()
        );
    }
}

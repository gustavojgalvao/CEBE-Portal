package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.dto.request.ProfessorRequestDTO;
import com.cebe.portal_aluno.dto.response.ProfessorResponseDTO;
import com.cebe.portal_aluno.entity.Professor;
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

        return new ProfessorResponseDTO(
                professorSalvo.getId(),
                professorSalvo.getNome(),
                professorSalvo.getEmail(),
                professorSalvo.getEspecializacao()
        );
    }
 
    public List<ProfessorResponseDTO> listar(){

        return professorRepository.findAll()
                .stream()
                .map(professor -> new ProfessorResponseDTO(
                        professor.getId(),
                        professor.getNome(),
                        professor.getEmail(),
                        professor.getEspecializacao()
                ))
                .toList();
    }
}

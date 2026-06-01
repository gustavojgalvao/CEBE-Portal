package com.cebe.portal_aluno.dto.response;

public record ProfessorResponseDTO(
    Integer id,
    String nome,
    String email,
    String especializacao
) {}

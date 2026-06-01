package com.cebe.portal_aluno.dto.request;

public record ProfessorRequestDTO(
    String nome,
    String email,
    String especializacao
) {}
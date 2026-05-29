package com.cebe.portal_aluno.dto.response;

public record AlunoResponseDTO(
    Integer id,
    String nome,
    String telefone,
    String cpf,
    String email
) {}

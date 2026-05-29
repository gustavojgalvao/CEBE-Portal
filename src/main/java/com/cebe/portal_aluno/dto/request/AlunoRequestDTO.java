package com.cebe.portal_aluno.dto.request;

public record AlunoRequestDTO(
    String nome,
    String telefone,
    String cpf,
    String email,
    String senha
) {}
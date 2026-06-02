package com.cebe.portal_aluno.dto.request;

public record AlunoRequestDTO(
    String nome,
    String telefone,
    String cpf,
    String email,
    String dataNascimento   // formato: DDMMYYYY — usada como senha inicial de acesso
) {}
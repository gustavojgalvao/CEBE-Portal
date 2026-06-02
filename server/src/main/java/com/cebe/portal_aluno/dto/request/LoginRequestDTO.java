package com.cebe.portal_aluno.dto.request;

public record LoginRequestDTO(
    String cpf,
    String dataNascimento   // formato: DDMMYYYY (ex: "01011990")
) {}

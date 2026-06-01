package com.cebe.portal_aluno.dto.response;

public record TurmaResponseDTO(
    Integer id,
    String turno,
    Integer lotacaoMaxima,
    Integer vagasOcupadas
) {}
package com.cebe.portal_aluno.dto.request;

public record TurmaRequestDTO(
    Integer idCursos,
    Integer idProfessor,
    String turno,
    Integer lotacaoMaxima,
    Integer vagasOcupadas
) {}

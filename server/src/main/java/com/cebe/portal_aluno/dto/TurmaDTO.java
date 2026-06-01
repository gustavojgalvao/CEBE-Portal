package com.cebe.portal_aluno.dto;

public record TurmaDTO(
    Integer idCurso,
    Integer idProfessor,
    String turno,
    Integer lotacaoMaxima,
    Integer vagasOcupadas
) {}

package com.cebe.portal_aluno.dto;

import java.time.LocalDate;

public record MatriculaDTO(
    Integer idAluno,
    Integer idTurma,
    LocalDate dataInscricao,
    String statusPagamento
) {}

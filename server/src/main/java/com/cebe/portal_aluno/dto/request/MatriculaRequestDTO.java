package com.cebe.portal_aluno.dto.request;

import java.time.LocalDate;

public record MatriculaRequestDTO(
    Integer idAluno,
    Integer idTurma,
    LocalDate dataInscricao,
    String statusPagamento
) {}

package com.cebe.portal_aluno.dto.response;

import java.time.LocalDate;

public record MatriculaResponseDTO(
    Integer id,
    LocalDate dataInscricao,
    String statusPagamento
) {}

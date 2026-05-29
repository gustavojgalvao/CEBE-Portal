package com.cebe.portal_aluno.dto;

import java.time.LocalDateTime;

public record AtendimentoDTO(
    Integer idAluno,
    String statusAtendimento,
    String mensagem,
    LocalDateTime dataHora
) {}

package com.cebe.portal_aluno.dto.request;

import java.time.LocalDateTime;

public record AtendimentoRequestDTO(
    Integer idAluno,
    String statusAtendimento,
    String mensagem,
    LocalDateTime dataHora
) {}

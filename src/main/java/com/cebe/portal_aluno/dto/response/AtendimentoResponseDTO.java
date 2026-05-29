package com.cebe.portal_aluno.dto.response;

import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
    Integer id,
    String statusAtendimento,
    String mensagem,
    LocalDateTime dataHora
) {}
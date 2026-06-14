package com.cebe.portal_aluno.dto.response;

import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
    Integer id,
    Integer alunoId,
    String alunoNome,
    String statusAtendimento,
    String mensagem,
    LocalDateTime dataHora
) {}
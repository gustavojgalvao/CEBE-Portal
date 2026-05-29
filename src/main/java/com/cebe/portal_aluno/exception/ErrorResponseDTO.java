package com.cebe.portal_aluno.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path,
    List<FieldErrorDTO> fields
) {}

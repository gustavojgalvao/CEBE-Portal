package com.cebe.portal_aluno.exception;

public record FieldErrorDTO(
    String field,
    String message
) {}

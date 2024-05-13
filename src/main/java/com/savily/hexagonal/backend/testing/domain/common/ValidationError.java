package com.savily.hexagonal.backend.testing.domain.common;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}

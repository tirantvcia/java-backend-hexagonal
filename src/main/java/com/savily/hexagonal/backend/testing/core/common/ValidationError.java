package com.savily.hexagonal.backend.testing.core.common;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}

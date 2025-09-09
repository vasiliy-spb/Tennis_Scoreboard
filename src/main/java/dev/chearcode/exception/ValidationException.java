package dev.chearcode.exception;

import jakarta.validation.ConstraintViolation;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        super(violations.stream()
                .map(v -> {
                    String key = v.getPropertyPath().toString();
                    String value = v.getMessage();
                    if (isExists(key)) {
                        return key + ": " + value;
                    }
                    return value;
                })
                .collect(Collectors.joining("; "))
        );
    }

    private static boolean isExists(String key) {
        return key != null && !key.isBlank();
    }
}

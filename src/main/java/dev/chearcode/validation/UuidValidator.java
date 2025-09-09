package dev.chearcode.validation;

import dev.chearcode.exception.ValidationException;

import java.util.UUID;

public final class UuidValidator {
    private UuidValidator() {
    }

    public static void validate(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new ValidationException("UUID must not be blank");
        }

        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("incorrect format UUID");
        }
    }
}

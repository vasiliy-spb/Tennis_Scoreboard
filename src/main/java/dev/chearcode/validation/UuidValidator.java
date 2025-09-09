package dev.chearcode.validation;

import dev.chearcode.exception.ValidationException;

import java.util.UUID;

public final class UuidValidator {

    private static final String BLANK_MESSAGE = "UUID must not be blank";
    private static final String INCORRECT_FORMAT_MESSAGE = "incorrect format UUID";

    private UuidValidator() {
    }

    public static void validate(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new ValidationException(BLANK_MESSAGE);
        }

        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(INCORRECT_FORMAT_MESSAGE);
        }
    }
}

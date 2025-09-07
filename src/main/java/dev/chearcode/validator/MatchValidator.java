package dev.chearcode.validator;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class MatchValidator implements ConstraintValidator<ValidMatch, Object> {
    private static final int MAX_NAME_LENGTH = 120;
    private static final int MAX_NAME_PART_LENGTH = 30;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateMatchRequestDto createMatchRequestDto) {

            String firstPlayerName = createMatchRequestDto.firstPlayerName().trim();
            String secondPlayerName = createMatchRequestDto.secondPlayerName().trim();

            if (!isCorrectName(firstPlayerName, context)) {
                return false;
            }

            if (!isCorrectName(secondPlayerName, context)) {
                return false;
            }

            return isDifferentName(firstPlayerName, secondPlayerName, context);
        }

        return false;
    }

    private boolean isDifferentName(String firstPlayerName, String secondPlayerName, ConstraintValidatorContext context) {
        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            setMessage(context, "names must be different");
            return false;
        }
        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean isCorrectName(String name, ConstraintValidatorContext context) {
        if (isBlank(name)) {
            setMessage(context, "name must not be blank");
            return false;
        }

        if (!hasCorrectLength(name)) {
            setMessage(context, "name length must be less then " + MAX_NAME_LENGTH);
            return false;
        }

        if (!hasCorrectPartsLength(name)) {
            setMessage(context, "name part length must be less then " + MAX_NAME_PART_LENGTH);
            return false;
        }
        return true;
    }

    private boolean isBlank(String name) {
        return name == null || name.isBlank();
    }

    private boolean hasCorrectLength(String name) {
        return name.length() <= MAX_NAME_LENGTH;
    }

    private boolean hasCorrectPartsLength(String name) {
        return Arrays.stream(name.split("\s+"))
                .map(String::length)
                .allMatch(l -> l <= MAX_NAME_PART_LENGTH);
    }
}

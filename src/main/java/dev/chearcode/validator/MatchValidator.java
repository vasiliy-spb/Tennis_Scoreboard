package dev.chearcode.validator;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Comparator;

public class MatchValidator implements ConstraintValidator<ValidMatch, Object> {
    private static final int MAX_NAME_LENGTH = 120;
    private static final int MAX_NAME_PART_LENGTH = 30;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateMatchRequestDto createMatchRequestDto) {

            String firstPlayerName = createMatchRequestDto.firstPlayerName().trim();
            String secondPlayerName = createMatchRequestDto.secondPlayerName().trim();

            if (!isCorrectName(context, firstPlayerName)) {
                return false;
            }

            if (!isCorrectName(context, secondPlayerName)) {
                return false;
            }

            return isDifferentName(firstPlayerName, secondPlayerName);
        }

        return false;
    }

    private boolean isDifferentName(String firstPlayerName, String secondPlayerName) {
        return !firstPlayerName.equalsIgnoreCase(secondPlayerName);
    }

    private boolean isCorrectName(ConstraintValidatorContext context, String name) {
        if (name == null || name.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name must not be blank")
                    .addConstraintViolation();
            return false;
        }

        int partLength = Arrays.stream(name.split("\s+"))
                .map(String::length)
                .max(Comparator.naturalOrder())
                .get();

        if (partLength > MAX_NAME_PART_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name part length must be less then " + MAX_NAME_PART_LENGTH)
                    .addConstraintViolation();
            return false;
        }

        if (name.length() > MAX_NAME_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name length must be less then " + MAX_NAME_LENGTH)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

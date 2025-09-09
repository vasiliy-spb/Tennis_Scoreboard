package dev.chearcode.validation;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class MatchValidator implements ConstraintValidator<ValidMatch, CreateMatchRequestDto> {
    private static final int MAX_NAME_LENGTH = 120;
    private static final int MAX_NAME_PART_LENGTH = 30;
    private static final String DIFFERENT_MESSAGE = "names must be different";
    private static final String BLANK_MESSAGE = "name must not be blank";
    private static final String NAME_LENGTH_MESSAGE = "name length must be less then " + MAX_NAME_LENGTH;
    private static final String NAME_PART_LENGTH_MESSAGE = "name part length must be less then " + MAX_NAME_PART_LENGTH;

    @Override
    public boolean isValid(CreateMatchRequestDto requestDto, ConstraintValidatorContext context) {
        String firstPlayerName = clearParam(requestDto.firstPlayerName());
        String secondPlayerName = clearParam(requestDto.secondPlayerName());

        if (!checkCorrectName(firstPlayerName, context)) {
            return false;
        }

        if (!checkCorrectName(secondPlayerName, context)) {
            return false;
        }

        return checkIfDifferentName(firstPlayerName, secondPlayerName, context);
    }

    private String clearParam(String param) {
        return param.trim();
    }

    private boolean checkIfDifferentName(String firstPlayerName, String secondPlayerName, ConstraintValidatorContext context) {
        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            setMessage(context, DIFFERENT_MESSAGE);
            return false;
        }
        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean checkCorrectName(String name, ConstraintValidatorContext context) {
        if (isBlank(name)) {
            setMessage(context, BLANK_MESSAGE);
            return false;
        }

        if (!hasCorrectLength(name)) {
            setMessage(context, NAME_LENGTH_MESSAGE);
            return false;
        }

        if (!hasCorrectPartsLength(name)) {
            setMessage(context, NAME_PART_LENGTH_MESSAGE);
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

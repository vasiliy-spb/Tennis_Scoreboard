package dev.chearcode.validation;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;

import static dev.chearcode.validation.Limits.*;
import static dev.chearcode.validation.Messages.*;

public class MatchValidator implements ConstraintValidator<ValidMatch, CreateMatchRequestDto> {
    private static final Pattern VALID_CHARS_PATTERN = Pattern.compile("^[\\p{L}\\s.'-]+$");
    private static final Pattern ISOLATED_SPECIAL_CHARS_PATTERN = Pattern.compile(".*\\s[.'-]\\s.*");
    private static final Pattern CONSECUTIVE_SPECIAL_CHARS_PATTERN = Pattern.compile(".*([.'-]{2,}).*");

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
        return param != null ? param.trim() : null;
    }

    private boolean checkIfDifferentName(String firstPlayerName, String secondPlayerName, ConstraintValidatorContext context) {
        if (firstPlayerName.equalsIgnoreCase(secondPlayerName)) {
            setMessage(context, SAME_NAME_MESSAGE.getMessage());
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
        if (isEmpty(name)) {
            setMessage(context, BLANK_NAME_MESSAGE.getMessage());
            return false;
        }

        if (hasTooLongLength(name)) {
            setMessage(context, TOO_LONG_NAME_MESSAGE.getMessage());
            return false;
        }

        if (hasTooSmallLength(name)) {
            setMessage(context, TOO_SMALL_NAME_MESSAGE.getMessage());
            return false;
        }

        if (hasTooLongPartLength(name)) {
            setMessage(context, TOO_LONG_NAME_PART_MESSAGE.getMessage());
            return false;
        }

        if (hasTooSmallPartLength(name)) {
            setMessage(context, TOO_SMALL_NAME_PART_MESSAGE.getMessage());
            return false;
        }

        if (containsDigits(name)) {
            setMessage(context, CONTAINS_DIGITS_MESSAGE.getMessage());
            return false;
        }

        if (!containsValidCharactersOnly(name)) {
            setMessage(context, INVALID_CHARACTERS_MESSAGE.getMessage());
            return false;
        }

        if (countCharacters(name, '.') > MAX_DOTS.getValue()) {
            setMessage(context, TOO_MANY_DOTS_MESSAGE.getMessage());
            return false;
        }

        if (countCharacters(name, '\'') > MAX_APOSTROPHES.getValue()) {
            setMessage(context, TOO_MANY_APOSTROPHES_MESSAGE.getMessage());
            return false;
        }

        if (countCharacters(name, '-') > MAX_HYPHENS.getValue()) {
            setMessage(context, TOO_MANY_HYPHENS_MESSAGE.getMessage());
            return false;
        }

        if (countLetters(name) < MIN_LETTERS.getValue()) {
            setMessage(context, TOO_FEW_LETTERS_MESSAGE.getMessage());
            return false;
        }

        if (hasMultipleSpaces(name)) {
            setMessage(context, MULTIPLE_SPACES_MESSAGE.getMessage());
            return false;
        }

        if (startsOrEndsWithInvalidCharacters(name)) {
            setMessage(context, INVALID_START_END_MESSAGE.getMessage());
            return false;
        }

        if (hasIsolatedSpecialCharacters(name)) {
            setMessage(context, ISOLATED_SPECIAL_CHAR_MESSAGE.getMessage());
            return false;
        }

        if (hasConsecutiveSpecialCharacters(name)) {
            setMessage(context, CONSECUTIVE_SPECIAL_CHARS_MESSAGE.getMessage());
            return false;
        }

        if (hasForbiddenCombinations(name)) {
            setMessage(context, FORBIDDEN_COMBINATION_MESSAGE.getMessage());
            return false;
        }

        return true;
    }

    private boolean isEmpty(String name) {
        return name == null || name.isEmpty();
    }

    private boolean hasTooLongLength(String name) {
        return name.length() > MAX_NAME_LENGTH.getValue();
    }

    private boolean hasTooSmallLength(String name) {
        return name.length() < MIN_NAME_LENGTH.getValue();
    }

    private boolean hasTooLongPartLength(String name) {
        return Arrays.stream(name.split("\\s+"))
                .filter(part -> !part.isEmpty())
                .map(String::length)
                .anyMatch(length -> length > MAX_NAME_PART_LENGTH.getValue());
    }

    private boolean hasTooSmallPartLength(String name) {
        return Arrays.stream(name.split("\\s+"))
                .filter(part -> !part.isEmpty())
                .map(String::length)
                .anyMatch(length -> length < MIN_NAME_PART_LENGTH.getValue());
    }

    private boolean containsDigits(String name) {
        return name.chars()
                .anyMatch(Character::isDigit);
    }

    private boolean containsValidCharactersOnly(String name) {
        return VALID_CHARS_PATTERN.matcher(name).matches();
    }

    private long countCharacters(String name, char character) {
        return name.chars().filter(ch -> ch == character).count();
    }

    private long countLetters(String name) {
        return name.chars().filter(Character::isLetter).count();
    }

    private boolean hasMultipleSpaces(String name) {
        return name.contains("  ");
    }

    private boolean startsOrEndsWithInvalidCharacters(String name) {
        char firstChar = name.charAt(0);
        char lastChar = name.charAt(name.length() - 1);

        return firstChar == ' ' || firstChar == '.' || firstChar == '\'' || firstChar == '-' ||
               lastChar == ' ' || lastChar == '.' || lastChar == '\'' || lastChar == '-';
    }

    private boolean hasIsolatedSpecialCharacters(String name) {
        return ISOLATED_SPECIAL_CHARS_PATTERN.matcher(name).matches();
    }

    private boolean hasConsecutiveSpecialCharacters(String name) {
        return CONSECUTIVE_SPECIAL_CHARS_PATTERN.matcher(name).matches();
    }

    private boolean hasForbiddenCombinations(String name) {
        return name.contains("-'") || name.contains("'-") ||
               name.contains("'.") || name.contains(".'") ||
               name.contains(".-") || name.contains("-.");
    }
}
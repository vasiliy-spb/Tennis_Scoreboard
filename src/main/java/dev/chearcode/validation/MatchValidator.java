package dev.chearcode.validation;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.regex.Pattern;

public class MatchValidator implements ConstraintValidator<ValidMatch, CreateMatchRequestDto> {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 120;
    private static final int MIN_NAME_PART_LENGTH = 2;
    private static final int MAX_NAME_PART_LENGTH = 30;
    private static final int MAX_DOTS = 2;
    private static final int MAX_APOSTROPHES = 2;
    private static final int MAX_HYPHENS = 2;
    private static final int MIN_LETTERS = 2;
    private static final String SAME_NAME_MESSAGE = "names must be different";
    private static final String BLANK_NAME_MESSAGE = "name must not be blank";
    private static final String TOO_LONG_NAME_MESSAGE = "name length must be less then " + MAX_NAME_LENGTH;
    private static final String TOO_LONG_NAME_PART_MESSAGE = "name part length must be less then " + MAX_NAME_PART_LENGTH;
    private static final String CONTAINS_DIGITS_MESSAGE = "name must not contains digits";
    private static final String INVALID_CHARACTERS_MESSAGE = "name contains invalid characters";
    private static final String TOO_MANY_DOTS_MESSAGE = "name must contain at most two dots";
    private static final String TOO_MANY_APOSTROPHES_MESSAGE = "name must contain at most two apostrophes";
    private static final String TOO_MANY_HYPHENS_MESSAGE = "name must contain at most two hyphens";
    private static final String TOO_FEW_LETTERS_MESSAGE = "name must contain at least two letters";
    private static final String MULTIPLE_SPACES_MESSAGE = "name must not contain multiple consecutive spaces";
    private static final String INVALID_START_END_MESSAGE = "name must not start or end with a space, dot, apostrophe or hyphen";
    private static final String ISOLATED_SPECIAL_CHAR_MESSAGE = "special characters must not be surrounded by spaces";
    private static final String CONSECUTIVE_SPECIAL_CHARS_MESSAGE = "special characters must not appear consecutively";
    private static final String FORBIDDEN_COMBINATION_MESSAGE = "apostrophe and hyphen must not appear next to each other";
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
            setMessage(context, SAME_NAME_MESSAGE);
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
            setMessage(context, BLANK_NAME_MESSAGE);
            return false;
        }

        if (!hasCorrectLength(name)) {
            setMessage(context, TOO_LONG_NAME_MESSAGE);
            return false;
        }

        if (!hasCorrectPartsLength(name)) {
            setMessage(context, TOO_LONG_NAME_PART_MESSAGE);
            return false;
        }

        if (containsDigits(name)) {
            setMessage(context, CONTAINS_DIGITS_MESSAGE);
            return false;
        }

        if (!containsValidCharacters(name)) {
            setMessage(context, INVALID_CHARACTERS_MESSAGE);
            return false;
        }

        if (countCharacters(name, '.') > MAX_DOTS) {
            setMessage(context, TOO_MANY_DOTS_MESSAGE);
            return false;
        }

        if (countCharacters(name, '\'') > MAX_APOSTROPHES) {
            setMessage(context, TOO_MANY_APOSTROPHES_MESSAGE);
            return false;
        }

        if (countCharacters(name, '-') > MAX_HYPHENS) {
            setMessage(context, TOO_MANY_HYPHENS_MESSAGE);
            return false;
        }

        if (countLetters(name) < MIN_LETTERS) {
            setMessage(context, TOO_FEW_LETTERS_MESSAGE);
            return false;
        }

        if (hasMultipleSpaces(name)) {
            setMessage(context, MULTIPLE_SPACES_MESSAGE);
            return false;
        }

        if (startsOrEndsWithInvalidChar(name)) {
            setMessage(context, INVALID_START_END_MESSAGE);
            return false;
        }

        if (hasIsolatedSpecialChars(name)) {
            setMessage(context, ISOLATED_SPECIAL_CHAR_MESSAGE);
            return false;
        }

        if (hasConsecutiveSpecialChars(name)) {
            setMessage(context, CONSECUTIVE_SPECIAL_CHARS_MESSAGE);
            return false;
        }

        if (hasForbiddenCombinations(name)) {
            setMessage(context, FORBIDDEN_COMBINATION_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isEmpty(String name) {
        return name == null || name.isEmpty();
    }

    private boolean hasCorrectLength(String name) {
        return name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH;
    }

    private boolean hasCorrectPartsLength(String name) {
        return Arrays.stream(name.split("\\s+"))
                .filter(part -> !part.isEmpty())
                .map(String::length)
                .allMatch(length -> length >= MIN_NAME_PART_LENGTH &&
                                    length <= MAX_NAME_PART_LENGTH);
    }

    private boolean containsDigits(String name) {
        return name.chars()
                .anyMatch(Character::isDigit);
    }

    private boolean containsValidCharacters(String name) {
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

    private boolean startsOrEndsWithInvalidChar(String name) {
        char firstChar = name.charAt(0);
        char lastChar = name.charAt(name.length() - 1);

        return firstChar == ' ' || firstChar == '.' || firstChar == '\'' || firstChar == '-' ||
               lastChar == ' ' || lastChar == '.' || lastChar == '\'' || lastChar == '-';
    }

    private boolean hasIsolatedSpecialChars(String name) {
        return ISOLATED_SPECIAL_CHARS_PATTERN.matcher(name).matches();
    }

    private boolean hasConsecutiveSpecialChars(String name) {
        return CONSECUTIVE_SPECIAL_CHARS_PATTERN.matcher(name).matches();
    }

    private boolean hasForbiddenCombinations(String name) {
        return name.contains("-'") || name.contains("'-") ||
               name.contains("'.") || name.contains(".'") ||
               name.contains(".-") || name.contains("-.");
    }
}

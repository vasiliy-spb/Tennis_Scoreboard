package dev.chearcode.validation;

public enum Messages {
    SAME_NAME_MESSAGE("names must be different"),
    BLANK_NAME_MESSAGE("name must not be blank"),
    TOO_LONG_NAME_MESSAGE("name length must be less then " + Limits.MAX_NAME_LENGTH.getValue()),
    TOO_LONG_NAME_PART_MESSAGE("name part length must be less then " + Limits.MAX_NAME_PART_LENGTH.getValue()),
    CONTAINS_DIGITS_MESSAGE("name must not contains digits"),
    INVALID_CHARACTERS_MESSAGE("name contains invalid characters"),
    TOO_MANY_DOTS_MESSAGE("name must contain at most two dots"),
    TOO_MANY_APOSTROPHES_MESSAGE("name must contain at most two apostrophes"),
    TOO_MANY_HYPHENS_MESSAGE("name must contain at most two hyphens"),
    TOO_FEW_LETTERS_MESSAGE("name must contain at least two letters"),
    MULTIPLE_SPACES_MESSAGE("name must not contain multiple consecutive spaces"),
    INVALID_START_END_MESSAGE("name must not start or end with a space, dot, apostrophe or hyphen"),
    ISOLATED_SPECIAL_CHAR_MESSAGE("special characters must not be surrounded by spaces"),
    CONSECUTIVE_SPECIAL_CHARS_MESSAGE("special characters must not appear consecutively"),
    FORBIDDEN_COMBINATION_MESSAGE("apostrophe and hyphen must not appear next to each other");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package dev.chearcode.validation;
public enum Limits {
    MIN_NAME_LENGTH(2),
    MAX_NAME_LENGTH(120),
    MIN_NAME_PART_LENGTH(2),
    MAX_NAME_PART_LENGTH(30),
    MAX_DOTS(2),
    MAX_APOSTROPHES(2),
    MAX_HYPHENS(2),
    MIN_LETTERS(2);

    private final int value;

    Limits(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

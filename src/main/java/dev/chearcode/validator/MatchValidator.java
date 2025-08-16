package dev.chearcode.validator;

import dev.chearcode.dto.CreateMatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchValidator implements ConstraintValidator<ValidMatch, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateMatchRequestDto createMatchRequestDto) {
            String firstPlayerName = createMatchRequestDto.firstPlayerName();
            String secondPlayerName = createMatchRequestDto.secondPlayerName();
            return !firstPlayerName.equalsIgnoreCase(secondPlayerName);
        }
        return false;
    }
}

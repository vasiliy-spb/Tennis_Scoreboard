package dev.chearcode.dto;

import dev.chearcode.validator.ValidMatch;

@ValidMatch
public record CreateMatchRequestDto(
        String firstPlayerName,
        String secondPlayerName
) {
}

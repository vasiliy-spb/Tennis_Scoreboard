package dev.chearcode.dto;

import dev.chearcode.validation.ValidMatch;

@ValidMatch
public record CreateMatchRequestDto(
        String firstPlayerName,
        String secondPlayerName
) {
}

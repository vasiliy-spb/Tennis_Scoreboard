package dev.chearcode.service;

import dev.chearcode.dto.CreateMatchRequestDto;
import dev.chearcode.model.TennisMatch;
import dev.chearcode.entity.Player;
import dev.chearcode.repository.PlayerRepository;

import java.util.UUID;

public class MatchService {
    private final PlayerRepository playerRepository;

    public MatchService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public UUID add(CreateMatchRequestDto requestDto) {
        String firstPlayerName = requestDto.firstPlayerName();
        String secondPlayerName = requestDto.secondPlayerName();

        Player firstPlayer = playerRepository.find(firstPlayerName)
                .orElse(new Player(firstPlayerName));
        Player secondPlayer = playerRepository.find(secondPlayerName)
                .orElse(new Player(secondPlayerName));

        TennisMatch match = new TennisMatch(firstPlayer, secondPlayer);

        // кладём матч в коллекцию текущих матчей

        return match.getId();
    }
}

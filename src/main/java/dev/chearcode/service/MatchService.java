package dev.chearcode.service;

import dev.chearcode.dto.CreateMatchRequestDto;
import dev.chearcode.model.Match;
import dev.chearcode.model.Player;
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
                .orElse(new Player(UUID.randomUUID(), firstPlayerName));
        Player secondPlayer = playerRepository.find(secondPlayerName)
                .orElse(new Player(UUID.randomUUID(), secondPlayerName));

        Match match = new Match(firstPlayer, secondPlayer);

        // кладём матч в коллекцию текущих матчей

        return match.getId();
    }
}

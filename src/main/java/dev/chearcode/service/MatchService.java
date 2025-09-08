package dev.chearcode.service;

import dev.chearcode.dto.CreateMatchRequestDto;
import dev.chearcode.entity.Match;
import dev.chearcode.entity.Player;
import dev.chearcode.model.TennisMatch;
import dev.chearcode.repository.MatchRepository;
import dev.chearcode.repository.PlayerRepository;

import java.util.List;
import java.util.UUID;

public class MatchService {
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final OngoingMatchesService ongoingMatchesService;

    public MatchService(PlayerRepository playerRepository, MatchRepository matchRepository, OngoingMatchesService ongoingMatchesService) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.ongoingMatchesService = ongoingMatchesService;
    }

    public UUID add(CreateMatchRequestDto requestDto) {
        Player firstPlayer = getPlayer(requestDto.firstPlayerName());
        Player secondPlayer = getPlayer(requestDto.secondPlayerName());

        TennisMatch tennisMatch = new TennisMatch(firstPlayer, secondPlayer);
        UUID matchId = UUID.randomUUID();
        ongoingMatchesService.addNewMatch(matchId, tennisMatch);
        return matchId;
    }

    private Player getPlayer(String name) {
        String clearName = name.trim();
        return playerRepository.findByName(clearName)
                .orElseGet(() -> createPlayer(clearName));
    }

    private Player createPlayer(String name) {
        Player player = new Player(name);
        UUID id = playerRepository.save(player);
        player.setId(id);
        return player;
    }

    public List<Match> getAll(int limit, int offset) {
        return matchRepository.findAll(limit, offset);
    }

    public List<Match> getAllByName(String name, int limit, int offset) {
        return matchRepository.findAllByPlayer(name, limit, offset);
    }

    public long countAll() {
        return matchRepository.countAll();
    }

    public long countAllByPlayer(String name) {
        return matchRepository.countAllByPlayer(name);
    }
}

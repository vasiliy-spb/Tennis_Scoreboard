package dev.chearcode.repository;

import dev.chearcode.model.Match;

import java.util.Optional;

public interface MatchRepository {
    Optional<Match> find(String firstPlayerName, String secondPlayerName);
}

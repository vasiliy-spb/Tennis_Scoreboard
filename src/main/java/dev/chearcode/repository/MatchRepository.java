package dev.chearcode.repository;

import dev.chearcode.model.TennisMatch;

import java.util.Optional;

public interface MatchRepository {
    Optional<TennisMatch> find(String firstPlayerName, String secondPlayerName);
}

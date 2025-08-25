package dev.chearcode.repository;

import dev.chearcode.model.TennisMatch;

import java.util.Optional;

public class MatchRepositoryImpl implements MatchRepository {
    @Override
    public Optional<TennisMatch> find(String firstPlayerName, String secondPlayerName) {
        return Optional.empty();
    }
}

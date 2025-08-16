package dev.chearcode.repository;

import dev.chearcode.model.Match;

import java.util.Optional;

public class MatchRepositoryImpl implements MatchRepository {
    @Override
    public Optional<Match> find(String firstPlayerName, String secondPlayerName) {
        return Optional.empty();
    }
}

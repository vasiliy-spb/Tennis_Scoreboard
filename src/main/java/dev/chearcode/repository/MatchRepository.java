package dev.chearcode.repository;

import dev.chearcode.entity.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends EntityRepository<Match> {
    Optional<Match> findByPlayers(String firstPlayerName, String secondPlayerName);

    List<Match> findAllByPlayer(String name, int limit, int offset);

    long countAll();

    long countAllByPlayer(String name);
}

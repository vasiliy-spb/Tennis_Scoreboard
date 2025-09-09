package dev.chearcode.repository;

import dev.chearcode.entity.Match;

import java.util.List;

public interface MatchRepository extends EntityRepository<Match> {
    List<Match> findAllByPlayer(String name, int limit, int offset);

    long countAll();

    long countAllByPlayer(String name);
}

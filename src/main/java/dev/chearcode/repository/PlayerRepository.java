package dev.chearcode.repository;

import dev.chearcode.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends EntityRepository<Player> {
    Optional<Player> findByName(String name);
}

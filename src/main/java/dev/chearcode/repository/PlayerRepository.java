package dev.chearcode.repository;

import dev.chearcode.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends EntityRepository<Player> {
    Optional<Player> findByName(String name);

    Optional<Player> findById(UUID id);
}

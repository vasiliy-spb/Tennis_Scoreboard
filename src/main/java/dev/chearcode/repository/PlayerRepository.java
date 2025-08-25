package dev.chearcode.repository;

import dev.chearcode.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> find(String playerName);
}

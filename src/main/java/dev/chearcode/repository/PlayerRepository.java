package dev.chearcode.repository;

import dev.chearcode.model.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> find(String playerName);
}

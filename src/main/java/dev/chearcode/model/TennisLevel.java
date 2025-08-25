package dev.chearcode.model;

import dev.chearcode.entity.Player;

public interface TennisLevel {
    void pointWonBy(Player player);

    boolean isFinished();

    Player getWinner();

    String getScoreValue(Player player);
}

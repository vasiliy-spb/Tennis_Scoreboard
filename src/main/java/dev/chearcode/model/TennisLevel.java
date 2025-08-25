package dev.chearcode.model;

public interface TennisLevel {
    void pointWonBy(Player player);

    boolean isFinished();

    Player getWinner();

    String getScoreValue(Player player);
}

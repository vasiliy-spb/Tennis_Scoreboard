package dev.chearcode.model;

public interface GameLevel {
    boolean isFinished();

    void pointWonBy(Player player);

    Player getWinner();

    int getScore(Player player);

    String getScoreValue(Player player);
}

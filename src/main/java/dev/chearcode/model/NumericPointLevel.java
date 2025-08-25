package dev.chearcode.model;

import dev.chearcode.entity.Player;

public abstract class NumericPointLevel extends BaseLevel<Integer> {
    private final int minScoreToWin;

    public NumericPointLevel(Player firstPlayer, Player secondPlayer, int minScoreToWin) {
        super(firstPlayer, secondPlayer);
        this.minScoreToWin = minScoreToWin;
    }

    @Override
    protected Integer getInitScore() {
        return 0;
    }

    @Override
    protected Player getWinnerByScore() {
        return scores.get(firstPlayer) > scores.get(secondPlayer) ? firstPlayer : secondPlayer;
    }

    @Override
    public String getScoreValue(Player player) {
        return String.valueOf(scores.get(player));
    }

    protected int getScoreDiff() {
        return Math.abs(scores.get(firstPlayer) - scores.get(secondPlayer));
    }

    protected boolean hasAnyoneAchieveVictoryScore() {
        return scores.get(firstPlayer) >= minScoreToWin ||
               scores.get(secondPlayer) >= minScoreToWin;
    }
}

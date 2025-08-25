package dev.chearcode.model;

import dev.chearcode.entity.Player;

public class TieBreak extends NumericPointLevel {
    private static final int MIN_SCORE_TO_WIN = 7;
    private static final int MIN_DIFF_TO_WIN = 2;

    public TieBreak(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
    }

    protected void addPoint(Player player) {
        scores.merge(player, 1, Integer::sum);
    }

    protected boolean finishCondition() {
        return hasAnyoneAchieveVictoryScore() && getScoreDiff() >= MIN_DIFF_TO_WIN;
    }
}

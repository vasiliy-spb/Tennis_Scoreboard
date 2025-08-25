package dev.chearcode.model;

import dev.chearcode.entity.Player;

public class TennisSet extends HighTennisLevel {
    private static final int MIN_SCORE_TO_WIN = 6;
    private static final int MIN_DIFF_TO_WIN = 2;
    private static final int ABS_SCORE_TO_WIN = 7;

    public TennisSet(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
    }

    @Override
    protected TennisLevel createInitialLevel() {
        return new Game(firstPlayer, secondPlayer);
    }

    @Override
    protected boolean finishCondition() {
        return isAbsoluteVictoryHere() ||
               hasAnyoneAchieveVictoryScore() && getScoreDiff() >= MIN_DIFF_TO_WIN;
    }

    private boolean isAbsoluteVictoryHere() {
        return scores.get(firstPlayer) >= ABS_SCORE_TO_WIN ||
               scores.get(secondPlayer) >= ABS_SCORE_TO_WIN;
    }

    @Override
    protected TennisLevel createNextLevel() {
        if (shouldPlayTieBreak()) {
            return new TieBreak(firstPlayer, secondPlayer);
        }
        return new Game(firstPlayer, secondPlayer);
    }

    private boolean shouldPlayTieBreak() {
        int firstScore = scores.get(firstPlayer);
        int secondScore = scores.get(secondPlayer);
        return firstScore == secondScore && firstScore == MIN_SCORE_TO_WIN;
    }
}

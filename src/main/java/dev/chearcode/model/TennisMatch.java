package dev.chearcode.model;

import dev.chearcode.entity.Player;

public class TennisMatch extends HighTennisLevel {
    private static final int MIN_SCORE_TO_WIN = 2;
    private static final int MIN_DIFF_TO_WIN = 1;

    public TennisMatch(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
    }

    @Override
    protected TennisLevel createInitialLevel() {
        return new TennisSet(firstPlayer, secondPlayer);
    }

    @Override
    protected boolean finishCondition() {
        return hasAnyoneAchieveVictoryScore() && getScoreDiff() >= MIN_DIFF_TO_WIN;
    }

    @Override
    protected TennisLevel createNextLevel() {
        return new TennisSet(firstPlayer, secondPlayer);
    }

    public String getCurrentSetValue(Player player) {
        TennisLevel set = getActiveSubLevel();
        return set.getScoreValue(player);
    }

    public String getCurrentGameValue(Player player) {
        TennisLevel subLevel = getActiveSubLevel();
        if (subLevel instanceof HighTennisLevel set) {
            TennisLevel game = set.getActiveSubLevel();
            return game.getScoreValue(player);
        }
        throw new UnsupportedOperationException("Impossible to get current game value");
    }
}

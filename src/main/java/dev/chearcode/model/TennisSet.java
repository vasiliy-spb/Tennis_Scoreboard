package dev.chearcode.model;

public class TennisSet extends AbstractTennisLevel {
    private static final int MIN_SCORE_TO_WIN = 6;
    private static final int MIN_DIFF_TO_WIN = 2;
    private static final int ABS_SCORE_TO_WIN = 7;

    public TennisSet(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
    }

    @Override
    protected TennisLevel createInitialLevel() {
        return new Game(firstPlayer, secondPlayer);
    }

    @Override
    protected boolean finishCondition() {
        if (isAbsoluteVictoryHere()) {
            return true;
        }
        if (hasAnyoneAchieveVictoryScore()) {
            return getScoreDiff() >= MIN_DIFF_TO_WIN;
        }
        return false;
    }

    private boolean isAbsoluteVictoryHere() {
        return scores.get(firstPlayer) >= ABS_SCORE_TO_WIN || scores.get(secondPlayer) >= ABS_SCORE_TO_WIN;
    }

    private boolean hasAnyoneAchieveVictoryScore() {
        return scores.get(firstPlayer) >= MIN_SCORE_TO_WIN || scores.get(secondPlayer) >= MIN_SCORE_TO_WIN;
    }

    @Override
    protected TennisLevel createNextLevel() {
        if (shouldPlayTieBreak()) {
            return new TieBreak(firstPlayer, secondPlayer);
        }
        return new Game(firstPlayer, secondPlayer);
    }

    private boolean shouldPlayTieBreak() {
        int firstWon = getWonCount(firstPlayer);
        int secondWon = getWonCount(secondPlayer);
        return firstWon == secondWon && firstWon == MIN_SCORE_TO_WIN;
    }

    private int getWonCount(Player player) {
        return (int) levels.stream()
                .filter(TennisLevel::isFinished)
                .filter(game -> game.getWinner().equals(player))
                .count();
    }
}

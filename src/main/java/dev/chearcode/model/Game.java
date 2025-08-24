package dev.chearcode.model;

public abstract class Game extends AbstractGameLevel {
    private static final int MIN_DIFF_TO_WIN = 2;

    protected Game(Player firstPlayer, Player secondPlayer, int minScoreToWin) {
        super(firstPlayer, secondPlayer, minScoreToWin);
    }

    @Override
    public void pointWonBy(Player player) {
        checkIfFinished();

        scores.merge(player, 1, Integer::sum);
    }

    @Override
    protected int getMinDiffToWin() {
        return MIN_DIFF_TO_WIN;
    }
}

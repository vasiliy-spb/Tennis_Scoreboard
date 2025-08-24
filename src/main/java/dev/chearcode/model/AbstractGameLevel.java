package dev.chearcode.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGameLevel implements GameLevel {
    private final int minScoreToWin;
    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final Map<Player, Integer> scores;

    protected AbstractGameLevel(Player firstPlayer, Player secondPlayer, int minScoreToWin) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.minScoreToWin = minScoreToWin;
        this.scores = new HashMap<>();
        scores.put(firstPlayer, 0);
        scores.put(secondPlayer, 0);
    }

    protected void checkIfFinished() {
        if (isFinished()) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " finished.");
        }
    }

    @Override
    public boolean isFinished() {
        if (scores.get(firstPlayer) >= minScoreToWin || scores.get(secondPlayer) >= minScoreToWin) {
            return Math.abs(scores.get(firstPlayer) - scores.get(secondPlayer)) >= getMinDiffToWin();
        }
        return false;
    }

    protected abstract int getMinDiffToWin();

    @Override
    public Player getWinner() {
        if (!isFinished()) {
            throw new IllegalStateException("There is no winner.");
        }
        return scores.get(firstPlayer) > scores.get(secondPlayer) ? firstPlayer : secondPlayer;
    }

    @Override
    public int getScore(Player player) {
        return scores.get(player);
    }

    @Override
    public String getScoreValue(Player player) {
        return String.valueOf(getScore(player));
    }
}

package dev.chearcode.model;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseLevel<T> implements TennisLevel {
    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final Map<Player, T> scores;

    public BaseLevel(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.scores = initScores();
    }

    private Map<Player, T> initScores() {
        return new HashMap<>(Map.of(
                firstPlayer, getInitScore(),
                secondPlayer, getInitScore())
        );
    }

    @Override
    public void pointWonBy(Player player) {
        checkState();

        addPoint(player);
    }

    private void checkState() {
        if (isFinished()) {
            throw new IllegalStateException(getClass().getSimpleName() + " already finished.");
        }
    }

    @Override
    public boolean isFinished() {
        return finishCondition();
    }

    @Override
    public Player getWinner() {
        if (!isFinished()) {
            throw new IllegalStateException("There is no winner yet.");
        }
        return getWinnerByScore();
    }

    protected abstract T getInitScore();

    protected abstract boolean finishCondition();

    protected abstract void addPoint(Player player);

    protected abstract Player getWinnerByScore();
}

package dev.chearcode.model;

import java.util.HashMap;
import java.util.Map;

public abstract class GameLevel<T> implements TennisLevel {
    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final Map<Player, T> points;

    public GameLevel(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.points = initPoints();
    }

    private Map<Player, T> initPoints() {
        return new HashMap<>(Map.of(
                firstPlayer, getInitPoint(),
                secondPlayer, getInitPoint())
        );
    }

    protected abstract T getInitPoint();

    @Override
    public void pointWonBy(Player player) {
        checkState();

        addPoint(player);
    }

    protected abstract void addPoint(Player player);

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

    protected abstract Player getWinnerByScore();

    protected abstract boolean finishCondition();
}

package dev.chearcode.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTennisLevel implements TennisLevel {
    protected final Player firstPlayer;
    protected final Player secondPlayer;
    protected final Map<Player, Integer> scores;
    protected List<TennisLevel> levels;

    protected AbstractTennisLevel(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.scores = new HashMap<>();
        scores.put(firstPlayer, 0);
        scores.put(secondPlayer, 0);
        this.levels = new ArrayList<>();
        levels.add(createInitialLevel());
    }

    @Override
    public void pointWonBy(Player player) {
        checkState();

        TennisLevel current = levels.get(levels.size() - 1);
        current.pointWonBy(player);

        if (current.isFinished()) {
            scores.merge(player, 1, Integer::sum);
            if (!isFinished()) {
                levels.add(createNextLevel());
            }
        }
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

    protected int getScoreDiff() {
        return Math.abs(scores.get(firstPlayer) - scores.get(secondPlayer));
    }

    protected abstract TennisLevel createInitialLevel();

    protected abstract boolean finishCondition();

    protected abstract TennisLevel createNextLevel();


    @Override
    public Player getWinner() {
        if (!isFinished()) {
            throw new IllegalStateException("There is no winner yet.");
        }
        return scores.get(firstPlayer) > scores.get(secondPlayer) ? firstPlayer : secondPlayer;
    }

    @Override
    public String getScoreValue(Player player) {
        return String.valueOf(scores.get(player));
    }

    public List<TennisLevel> getLevels() {
        return List.copyOf(levels);
    }
}

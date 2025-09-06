package dev.chearcode.model;

import dev.chearcode.entity.Player;

import java.util.List;
import java.util.Stack;

public abstract class HighTennisLevel extends NumericPointLevel {
    protected Stack<TennisLevel> levels;

    protected HighTennisLevel(Player firstPlayer, Player secondPlayer, int minScoreToWin) {
        super(firstPlayer, secondPlayer, minScoreToWin);
        this.levels = new Stack<>();
        levels.add(createInitialLevel());
    }

    @Override
    protected void addPoint(Player player) {
        TennisLevel current = levels.peek();
        current.pointWonBy(player);

        if (current.isFinished()) {
            scores.merge(player, 1, Integer::sum);
            if (!isFinished()) {
                levels.add(createNextLevel());
            }
        }
    }

    public List<TennisLevel> getLevels() {
        return List.copyOf(levels);
    }

    public TennisLevel getActiveSubLevel() {
        return levels.peek();
    }

    protected abstract TennisLevel createInitialLevel();

    protected abstract TennisLevel createNextLevel();
}

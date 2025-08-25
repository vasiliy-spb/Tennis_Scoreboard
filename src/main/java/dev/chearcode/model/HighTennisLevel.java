package dev.chearcode.model;

import java.util.ArrayList;
import java.util.List;

public abstract class HighTennisLevel extends NumericPointLevel {
    protected List<TennisLevel> levels;

    protected HighTennisLevel(Player firstPlayer, Player secondPlayer, int minScoreToWin) {
        super(firstPlayer, secondPlayer, minScoreToWin);
        this.levels = new ArrayList<>();
        levels.add(createInitialLevel());
    }

    @Override
    protected void addPoint(Player player) {
        TennisLevel current = levels.get(levels.size() - 1);
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

    protected abstract TennisLevel createInitialLevel();

    protected abstract TennisLevel createNextLevel();
}

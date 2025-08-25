package dev.chearcode.model;

import java.util.Objects;
import java.util.UUID;

public class Match extends HighTennisLevel {
    private static final int MIN_SCORE_TO_WIN = 2;
    private static final int MIN_DIFF_TO_WIN = 1;
    private final UUID id;

    public Match(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
        this.id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

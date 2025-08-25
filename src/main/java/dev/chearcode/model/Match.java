package dev.chearcode.model;

import java.util.UUID;

public class Match extends AbstractTennisLevel {
    private static final int MIN_SCORE_TO_WIN = 2;
    private static final int MIN_DIFF_TO_WIN = 1;
    private final UUID id;

    public Match(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
        this.id = UUID.randomUUID();
    }

    @Override
    protected TennisLevel createInitialLevel() {
        return new TennisSet(firstPlayer, secondPlayer);
    }

    @Override
    protected boolean finishCondition() {
        if (hasAnyoneAchieveVictoryScore()) {
            return getScoreDiff() >= MIN_DIFF_TO_WIN;
        }
        return false;
    }

    private boolean hasAnyoneAchieveVictoryScore() {
        return scores.get(firstPlayer) >= MIN_SCORE_TO_WIN || scores.get(secondPlayer) >= MIN_SCORE_TO_WIN;
    }

    @Override
    protected TennisLevel createNextLevel() {
        return new TennisSet(firstPlayer, secondPlayer);
    }

    public UUID getId() {
        return id;
    }
}

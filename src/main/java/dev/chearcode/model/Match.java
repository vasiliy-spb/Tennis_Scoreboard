package dev.chearcode.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match extends AbstractGameLevel {
    private static final int MIN_SCORE_TO_WIN = 2;
    private static final int MIN_DIFF_TO_WIN = 1;
    private final UUID uuid;
    private final List<Set> sets;

    public Match(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
        this.uuid = UUID.randomUUID();
        this.sets = new ArrayList<>();
        sets.add(new Set(firstPlayer, secondPlayer));
    }

    @Override
    protected int getMinDiffToWin() {
        return MIN_DIFF_TO_WIN;
    }

    @Override
    public void pointWonBy(Player player) {
        checkIfFinished();

        Set currentSet = sets.get(sets.size() - 1);
        currentSet.pointWonBy(player);
        if (!currentSet.isFinished()) {
            return;
        }
        scores.merge(player, 1, Integer::sum);
        if (!isFinished()) {
            sets.add(new Set(firstPlayer, secondPlayer));
        }
    }

    public List<Set> getSets() {
        return sets;
    }

    public UUID getUuid() {
        return uuid;
    }
}

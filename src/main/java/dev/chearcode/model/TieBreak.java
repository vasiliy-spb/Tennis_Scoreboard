package dev.chearcode.model;

public class TieBreak extends Game {
    private static final int MIN_SCORE_TO_WIN = 7;

    public TieBreak(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
    }

    public String getScoreValue(Player player) {
        return String.valueOf(getScore(player));
    }
}

package dev.chearcode.model;

public class TieBreak extends GameLevel<Integer> {
    private static final int MIN_SCORE_TO_WIN = 7;
    private static final int MIN_DIFF_TO_WIN = 2;

    public TieBreak(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
    }

    protected Integer getInitPoint() {
        return 0;
    }

    protected void addPoint(Player player) {
        points.merge(player, 1, Integer::sum);
    }

    @Override
    public String getScoreValue(Player player) {
        return String.valueOf(points.get(player));
    }

    private int getMinDiffToWin() {
        return MIN_DIFF_TO_WIN;
    }

    protected boolean finishCondition() {
        if (hasAnyoneAchieveVictoryScore()) {
            return getScoreDiff() >= getMinDiffToWin();
        }
        return false;
    }

    protected int getScoreDiff() {
        return Math.abs(points.get(firstPlayer) - points.get(secondPlayer));
    }

    protected boolean hasAnyoneAchieveVictoryScore() {
        return points.get(firstPlayer) >= MIN_SCORE_TO_WIN || points.get(secondPlayer) >= MIN_SCORE_TO_WIN;
    }

    @Override
    protected Player getWinnerByScore() {
        return points.get(firstPlayer) > points.get(secondPlayer) ? firstPlayer : secondPlayer;
    }
}

package dev.chearcode.model;

public class RegularGame extends Game {
    private static final int MIN_SCORE_TO_WIN = 4;

    public RegularGame(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
    }

    @Override
    public void pointWonBy(Player player) {
        checkIfFinished();

        Player opponent = getOpponent(player);
        if (hasAdvantage(opponent)) {
            lostAdvantage(opponent);
        } else {
            scores.merge(player, 1, Integer::sum);
        }
    }

    private Player getOpponent(Player player) {
        return player.equals(firstPlayer) ? secondPlayer : firstPlayer;
    }

    private boolean hasAdvantage(Player other) {
        return getScoreValue(other).equals("AD");
    }

    private void lostAdvantage(Player player) {
        scores.merge(player, -1, Integer::sum);
    }

    public String getScoreValue(Player player) {
        int score = getScore(player);
        return switch (score) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            case 4 -> "AD";
            default -> throw new IllegalArgumentException("Has no value for score: " + score);
        };
    }
}

package dev.chearcode.model;

import dev.chearcode.entity.Player;

public class Game extends BaseLevel<Game.Point> {
    private boolean finished = false;

    public Game(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
    }

    protected Point getInitScore() {
        return Point.LOVE;
    }

    protected void addPoint(Player player) {
        Player opponent = getOpponent(player);
        Point playersPoint = scores.get(player);
        Point opponentsPoint = scores.get(opponent);

        switch (playersPoint) {
            case LOVE -> scores.put(player, Point.FIFTEEN);
            case FIFTEEN -> scores.put(player, Point.THIRTY);
            case THIRTY -> scores.put(player, Point.FORTY);
            case FORTY -> {
                if (opponentsPoint == Point.FORTY) {
                    scores.put(player, Point.ADVANTAGE);
                } else if (opponentsPoint == Point.ADVANTAGE) {
                    scores.put(opponent, Point.FORTY);
                } else {
                    finished = true;
                }
            }
            case ADVANTAGE -> finished = true;
        }
    }

    protected boolean finishCondition() {
        return finished;
    }

    @Override
    protected Player getWinnerByScore() {
        return scores.get(firstPlayer).ordinal() > scores.get(secondPlayer).ordinal() ?
                firstPlayer : secondPlayer;
    }

    private Player getOpponent(Player player) {
        return player.equals(firstPlayer) ?
                secondPlayer : firstPlayer;
    }

    @Override
    public String getScoreValue(Player player) {
        if (finished && player.equals(getWinner())) {
            return "GAME";
        }
        return scores.get(player).getDisplay();
    }

    protected enum Point {
        LOVE("0"),
        FIFTEEN("15"),
        THIRTY("30"),
        FORTY("40"),
        ADVANTAGE("AD");

        private final String display;

        Point(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}

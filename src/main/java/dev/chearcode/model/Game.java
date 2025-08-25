package dev.chearcode.model;

public class Game extends GameLevel<Game.Point> {
    private boolean finished = false;

    public Game(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer);
    }

    protected Point getInitPoint() {
        return Point.LOVE;
    }

    protected void addPoint(Player player) {
        Player opponent = getOpponent(player);
        Point playersPoint = points.get(player);
        Point opponentsPoint = points.get(opponent);

        switch (playersPoint) {
            case LOVE -> points.put(player, Point.FIFTEEN);
            case FIFTEEN -> points.put(player, Point.THIRTY);
            case THIRTY -> points.put(player, Point.FORTY);
            case FORTY -> {
                if (opponentsPoint == Point.FORTY) {
                    points.put(player, Point.ADVANTAGE);
                } else if (opponentsPoint == Point.ADVANTAGE) {
                    points.put(opponent, Point.FORTY);
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
        return points.get(firstPlayer).ordinal() > points.get(secondPlayer).ordinal() ? firstPlayer : secondPlayer;
    }

    private Player getOpponent(Player player) {
        return player.equals(firstPlayer) ? secondPlayer : firstPlayer;
    }

    @Override
    public String getScoreValue(Player player) {
        if (finished && player.equals(getWinner())) {
            return "GAME";
        }
        return points.get(player).getDisplay();
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

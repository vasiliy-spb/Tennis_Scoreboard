package dev.chearcode.model;

import java.util.ArrayList;
import java.util.List;

public class Set extends AbstractGameLevel {
    private static final int MIN_SCORE_TO_WIN = 6;
    private static final int MIN_DIFF_TO_WIN = 2;
    private static final int ABS_SCORE_TO_WIN = 7;
    private final List<Game> games;

    public Set(Player firstPlayer, Player secondPlayer) {
        super(firstPlayer, secondPlayer, MIN_SCORE_TO_WIN);
        this.games = new ArrayList<>();
        games.add(new RegularGame(firstPlayer, secondPlayer));
    }

    @Override
    protected int getMinDiffToWin() {
        return MIN_DIFF_TO_WIN;
    }

    @Override
    public void pointWonBy(Player player) {
        checkIfFinished();

        Game currentGame = games.get(games.size() - 1);
        currentGame.pointWonBy(player);
        if (!currentGame.isFinished()) {
            return;
        }

        scores.merge(player, 1, Integer::sum);
        if (isFinished()) {
            return;
        }

        if (shouldPlayTieBreak()) {
            games.add(new TieBreak(firstPlayer, secondPlayer));
        } else {
            games.add(new RegularGame(firstPlayer, secondPlayer));
        }
    }

    @Override
    public boolean isFinished() {
        if (scores.get(firstPlayer) >= ABS_SCORE_TO_WIN || scores.get(secondPlayer) >= ABS_SCORE_TO_WIN) {
            return true;
        }
        if (scores.get(firstPlayer) >= MIN_SCORE_TO_WIN || scores.get(secondPlayer) >= MIN_SCORE_TO_WIN) {
            return Math.abs(scores.get(firstPlayer) - scores.get(secondPlayer)) >= getMinDiffToWin();
        }
        return false;
    }

    private boolean shouldPlayTieBreak() {
        int firstWon = getWonCount(firstPlayer);
        int secondWon = getWonCount(secondPlayer);
        return firstWon == secondWon && firstWon == MIN_SCORE_TO_WIN;
    }

    private int getWonCount(Player player) {
        return (int) games.stream()
                .filter(game -> game.getWinner().equals(player))
                .count();
    }

    public List<Game> getGames() {
        return games;
    }
}

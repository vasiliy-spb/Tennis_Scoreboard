package dev.chearcode.new_model;

import dev.chearcode.new_model.score.GameScore;
import dev.chearcode.new_model.score.MatchScore;
import dev.chearcode.new_model.score.Score;
import dev.chearcode.new_model.score.SetScore;

import java.util.Map;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Map<Player, Score> scores;

    public Match(UUID id, Player firstPlayer, Player secondPlayer) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.scores = Map.of(
                firstPlayer, new Score(),
                secondPlayer, new Score()
        );
    }

    public void showResult() {
        System.out.println("--------------------- R E S U L T S ---------------------");
        System.out.printf("Player: %s\t\t\tPlayer: %s\n", firstPlayer.getName(), secondPlayer.getName());
        System.out.printf("Game:   %s\t\t\tGame:   %s\n", scores.get(firstPlayer).getGameScore(), scores.get(secondPlayer).getGameScore());
        System.out.printf("Set:    %s\t\t\tSet:    %s\n", scores.get(firstPlayer).getSetScore(), scores.get(secondPlayer).getSetScore());
        System.out.printf("Match:  %s\t\t\tMatch:  %s\n", scores.get(firstPlayer).getMatchScore(), scores.get(secondPlayer).getMatchScore());
    }

    public void addPoint(Player player) {
        if (isGameOver()) {
            return;
        }

        Score winnerScore = getWinnerScore(player);
        Score loserScore = getLoserScore(player);

        addGamePoint(winnerScore);

        if (loserScore.getGameScore() == GameScore.ADVANTAGE) {
            if (winnerScore.getGameScore() == GameScore.FORTY || winnerScore.getGameScore() == GameScore.ADVANTAGE) {
                loserScore.setGameScore(GameScore.DEUCE);
                winnerScore.setGameScore(GameScore.DEUCE);
            } else {
                loserScore.setGameScore(GameScore.FORTY);
            }
        }

        if (winnerScore.getGameScore() == GameScore.GAME) {
            resetGameScore(winnerScore, loserScore);
            addSetPoint(winnerScore);

            if (winnerScore.getSetScore() == SetScore.SET) {
                resetSetScore(winnerScore, loserScore);
                addMatchPoint(winnerScore);
            }
        } else if (winnerScore.getGameScore() == GameScore.FORTY) {
            if (loserScore.getGameScore() == GameScore.FORTY) {
                winnerScore.setGameScore(GameScore.DEUCE);
                loserScore.setGameScore(GameScore.DEUCE);
            }
        }
    }

    private void resetSetScore(Score firstPlayerScore, Score secondPlayerScore) {
        firstPlayerScore.resetSetScore();
        secondPlayerScore.resetSetScore();
    }

    private void resetGameScore(Score firstPlayerScore, Score secondPlayerScore) {
        firstPlayerScore.resetGameScore();
        secondPlayerScore.resetGameScore();
    }

    private void addGamePoint(Score score) {
        int currentScoreIndex = score.getGameScore().ordinal();
        GameScore nextScore = GameScore.values()[currentScoreIndex + 1];
        if (nextScore == GameScore.DEUCE) {
            nextScore = GameScore.ADVANTAGE;
        }
        score.setGameScore(nextScore);
    }

    private void addSetPoint(Score score) {
        int currentScoreIndex = score.getSetScore().ordinal();
        SetScore nextScore = SetScore.values()[currentScoreIndex + 1];
        score.setSetScore(nextScore);
    }

    private void addMatchPoint(Score score) {
        int currentScoreIndex = score.getMatchScore().ordinal();
        MatchScore nextScore = MatchScore.values()[currentScoreIndex + 1];
        score.setMatchScore(nextScore);
    }

    private Score getLoserScore(Player winner) {
        Player loser = getLoser(winner);
        return scores.get(loser);
    }

    private Score getWinnerScore(Player player) {
        return scores.get(player);
    }

    private Player getLoser(Player winner) {
        return winner.getName().equals(firstPlayer.getName()) ? secondPlayer : firstPlayer;
    }

    public boolean isGameOver() {
        return scores.get(firstPlayer).getMatchScore().equals(MatchScore.MATCH) ||
               scores.get(secondPlayer).getMatchScore().equals(MatchScore.MATCH);
    }

    public Score getScore(Player player) {
        return scores.get(player);
    }

    public UUID getId() {
        return id;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }
}

package dev.chearcode.model;

import dev.chearcode.model.score.GameScoreValue;
import dev.chearcode.model.score.MatchScoreValue;
import dev.chearcode.model.score.PlayerScore;
import dev.chearcode.model.score.SetScoreValue;

import java.util.Map;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Map<Player, PlayerScore> scores;

    public Match(UUID id, Player firstPlayer, Player secondPlayer) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.scores = Map.of(
                firstPlayer, new PlayerScore(),
                secondPlayer, new PlayerScore()
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

        PlayerScore winnerPlayerScore = getWinnerScore(player);
        PlayerScore loserPlayerScore = getLoserScore(player);

        addGamePoint(winnerPlayerScore);

        if (loserPlayerScore.getGameScore() == GameScoreValue.ADVANTAGE) {
            if (winnerPlayerScore.getGameScore() == GameScoreValue.FORTY || winnerPlayerScore.getGameScore() == GameScoreValue.ADVANTAGE) {
                loserPlayerScore.setGameScore(GameScoreValue.DEUCE);
                winnerPlayerScore.setGameScore(GameScoreValue.DEUCE);
            } else {
                loserPlayerScore.setGameScore(GameScoreValue.FORTY);
            }
        }

        if (winnerPlayerScore.getGameScore() == GameScoreValue.GAME) {
            resetGameScore(winnerPlayerScore, loserPlayerScore);
            addSetPoint(winnerPlayerScore);

            if (winnerPlayerScore.getSetScore() == SetScoreValue.SET) {
                resetSetScore(winnerPlayerScore, loserPlayerScore);
                addMatchPoint(winnerPlayerScore);
            }
        } else if (winnerPlayerScore.getGameScore() == GameScoreValue.FORTY) {
            if (loserPlayerScore.getGameScore() == GameScoreValue.FORTY) {
                winnerPlayerScore.setGameScore(GameScoreValue.DEUCE);
                loserPlayerScore.setGameScore(GameScoreValue.DEUCE);
            }
        }
    }

    private void resetSetScore(PlayerScore firstPlayerPlayerScore, PlayerScore secondPlayerPlayerScore) {
        firstPlayerPlayerScore.resetSetScore();
        secondPlayerPlayerScore.resetSetScore();
    }

    private void resetGameScore(PlayerScore firstPlayerPlayerScore, PlayerScore secondPlayerPlayerScore) {
        firstPlayerPlayerScore.resetGameScore();
        secondPlayerPlayerScore.resetGameScore();
    }

    private void addGamePoint(PlayerScore playerScore) {
        int currentScoreIndex = playerScore.getGameScore().ordinal();
        GameScoreValue nextScore = GameScoreValue.values()[currentScoreIndex + 1];
        if (nextScore == GameScoreValue.DEUCE) {
            nextScore = GameScoreValue.ADVANTAGE;
        }
        playerScore.setGameScore(nextScore);
    }

    private void addSetPoint(PlayerScore playerScore) {
        int currentScoreIndex = playerScore.getSetScore().ordinal();
        SetScoreValue nextScore = SetScoreValue.values()[currentScoreIndex + 1];
        playerScore.setSetScore(nextScore);
    }

    private void addMatchPoint(PlayerScore playerScore) {
        int currentScoreIndex = playerScore.getMatchScore().ordinal();
        MatchScoreValue nextScore = MatchScoreValue.values()[currentScoreIndex + 1];
        playerScore.setMatchScore(nextScore);
    }

    private PlayerScore getLoserScore(Player winner) {
        Player loser = getLoser(winner);
        return scores.get(loser);
    }

    private PlayerScore getWinnerScore(Player player) {
        return scores.get(player);
    }

    private Player getLoser(Player winner) {
        return winner.getName().equals(firstPlayer.getName()) ? secondPlayer : firstPlayer;
    }

    public boolean isGameOver() {
        return scores.get(firstPlayer).getMatchScore().equals(MatchScoreValue.MATCH) ||
               scores.get(secondPlayer).getMatchScore().equals(MatchScoreValue.MATCH);
    }

    public PlayerScore getScore(Player player) {
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

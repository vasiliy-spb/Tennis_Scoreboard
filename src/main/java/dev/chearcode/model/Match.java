package dev.chearcode.model;

import dev.chearcode.model.score.*;

import java.util.UUID;

public class Match {
    private final UUID id;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final MatchScore scores;

    public Match(UUID id, Player firstPlayer, Player secondPlayer) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.scores = new MatchScore(id, new PlayerScore(), new PlayerScore());
    }

    public void showResult() {
        System.out.println("--------------------- R E S U L T S ---------------------");
        System.out.printf("Player: %s\t\t\tPlayer: %s\n", firstPlayer.getName(), secondPlayer.getName());
        System.out.printf("Game:   %s\t\t\tGame:   %s\n", scores.getFirst().getGameScore(), scores.getSecond().getGameScore());
        System.out.printf("Set:    %s\t\t\tSet:    %s\n", scores.getFirst().getSetScore(), scores.getSecond().getSetScore());
        System.out.printf("Match:  %s\t\t\tMatch:  %s\n", scores.getFirst().getMatchScore(), scores.getSecond().getMatchScore());
    }

    public void addPoint(Player player) {
        if (isGameOver()) {
            return;
        }

        PlayerScore winnerPlayerScore = getWinnerScore(player);
        PlayerScore loserPlayerScore = getLoserScore(player);

        switch (winnerPlayerScore.getGameScore()) {
            case LOVE, FIFTEEN, THIRTY -> {
                addGamePoint(winnerPlayerScore);
                if (winnerPlayerScore.getGameScore() == GameScoreValue.FORTY && loserPlayerScore.getGameScore() == GameScoreValue.FORTY) {
                    winnerPlayerScore.setGameScore(GameScoreValue.DEUCE);
                    loserPlayerScore.setGameScore(GameScoreValue.DEUCE);
                }
            }
            case FORTY -> {
                if (loserPlayerScore.getGameScore().ordinal() < GameScoreValue.FORTY.ordinal()) {
                    winnerPlayerScore.setGameScore(GameScoreValue.GAME);
                } else {
                    winnerPlayerScore.setGameScore(GameScoreValue.ADVANTAGE);
                    loserPlayerScore.setGameScore(GameScoreValue.DEUCE);
                }
            }
            case DEUCE -> {
                if (loserPlayerScore.getGameScore() == GameScoreValue.ADVANTAGE) {
                    loserPlayerScore.setGameScore(GameScoreValue.DEUCE);
                } else {
                    winnerPlayerScore.setGameScore(GameScoreValue.ADVANTAGE);
                }
            }
            case ADVANTAGE -> winnerPlayerScore.setGameScore(GameScoreValue.GAME);
            default ->
                    throw new IllegalArgumentException("Unexpected GameScoreValue: " + winnerPlayerScore.getGameScore());
        }

        checkIfWonSet(winnerPlayerScore, loserPlayerScore);
        checkIfWonMatch(winnerPlayerScore, loserPlayerScore);
    }

    private void checkIfWonMatch(PlayerScore winnerPlayerScore, PlayerScore loserPlayerScore) {
        if (winnerPlayerScore.getSetScore() == SetScoreValue.SET || winnerPlayerScore.getSetScore() == SetScoreValue.SIX && loserPlayerScore.getSetScore().ordinal() <= SetScoreValue.FOUR.ordinal()) {
            resetSetScore(winnerPlayerScore, loserPlayerScore);
            addMatchPoint(winnerPlayerScore);
        }
    }

    private void checkIfWonSet(PlayerScore winnerPlayerScore, PlayerScore loserPlayerScore) {
        if (winnerPlayerScore.getGameScore() == GameScoreValue.GAME) {
            resetGameScore(winnerPlayerScore, loserPlayerScore);
            addSetPoint(winnerPlayerScore);
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
        if (winner.getName().equals(firstPlayer.getName())) {
            return scores.getSecond();
        }
        return scores.getFirst();
    }

    private PlayerScore getWinnerScore(Player player) {
        if (player.getName().equals(firstPlayer.getName())) {
            return scores.getFirst();
        }
        return scores.getSecond();
    }

    public boolean isGameOver() {
        return scores.getFirst().getMatchScore().equals(MatchScoreValue.MATCH) ||
               scores.getSecond().getMatchScore().equals(MatchScoreValue.MATCH);
    }

    public PlayerScore getScore(Player player) {
        if (player.getName().equals(firstPlayer.getName())) {
            return scores.getFirst();
        }
        return scores.getSecond();
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

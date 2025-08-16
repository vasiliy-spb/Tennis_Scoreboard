package model;

import dev.chearcode.model.*;
import dev.chearcode.model.score.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match match;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        match = new Match(player1, player2);
    }

    @Test
    void testInitialScore() {
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
        assertEquals(MatchScoreValue.ZERO, match.getScore(player1).getMatchScore());
    }

    @Test
    void testSimpleGameWin() {
        // Player1 wins 4 points
        winPoints(player1, 4);

        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    @Test
    void testDeuceAndAdvantage() {
        // Both players reach 40-40
        winPoints(player1, 3);
        winPoints(player2, 3);

        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());

        // Player1 gets advantage
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());

        // Player2 saves advantage
        match.addPoint(player2);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());

        // Player1 gets advantage again and wins
        winPoints(player1, 2);
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    @Test
    void testSetWin_6_4() {
        // Player2 wins 4 games
        winGames(player2, 4);
        // Player1 wins 6 games
        winGames(player1, 6);

        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
    }

    @Test
    void testSetWin_7_5() {
        // Player2 wins 5 games
        winGames(player2, 5);
        // Player1 wins 7 games
        winGames(player1, 7);

        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
    }

    @Test
    void testMatchWin() {
        // Player1 wins first set 6-4
        winSet(player1, 6, player2, 4);
        // Player1 wins second set 6-4
        winSet(player1, 6, player2, 4);

        assertEquals(MatchScoreValue.MATCH, match.getScore(player1).getMatchScore());
        assertTrue(match.isGameOver());
    }

    @Test
    void testNoPointsAfterMatchOver() {
        winSet(player1, 6, player2, 4);
        winSet(player1, 6, player2, 4);

        GameScoreValue gameScore = match.getScore(player1).getGameScore();
        SetScoreValue setScore = match.getScore(player1).getSetScore();
        MatchScoreValue matchScore = match.getScore(player1).getMatchScore();

        match.addPoint(player1);

        assertEquals(gameScore, match.getScore(player1).getGameScore());
        assertEquals(setScore, match.getScore(player1).getSetScore());
        assertEquals(matchScore, match.getScore(player1).getMatchScore());
    }

    @Test
    void testSetNotWonAt6_5() {
        winGames(player2, 5);
        winGames(player1, 6);

        assertEquals(SetScoreValue.SIX, match.getScore(player1).getSetScore());
        assertEquals(SetScoreValue.FIVE, match.getScore(player2).getSetScore());
        assertEquals(MatchScoreValue.ZERO, match.getScore(player1).getMatchScore());
    }

    @Test
    void testScoreAfterSetWin() {
        winSet(player1, 6, player2, 4);

        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
    }

    @Test
    void testTieBreakAt6_6() {
        // Доводим сет до 6-6
        winGames(player1, 5);
        winGames(player2, 5);
        winGames(player1, 1);
        winGames(player2, 1);

        // Проверяем, что сет не закончен
        assertFalse(match.isGameOver());
        assertEquals(SetScoreValue.SIX, match.getScore(player1).getSetScore());
        assertEquals(SetScoreValue.SIX, match.getScore(player2).getSetScore());

        // Player1 выигрывает 7-й гейм (тай-брейк)
        winPoints(player1, 4);
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player2).getSetScore());
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
    }

    @Test
    void testSetLoss6_7() {
        // Player1 ведёт 6-5
        winGames(player1, 6);
        winGames(player2, 5);

        // Player2 выигрывает два гейма подряд (6-6 и 7-6)
        winPoints(player2, 4); // 6-6
        winPoints(player2, 4); // 6-7

        assertEquals(MatchScoreValue.ONE, match.getScore(player2).getMatchScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
    }

    @Test
    void testComplexDeuceScenario() {
        // Доводим до 40-40
        winPoints(player1, 3);
        winPoints(player2, 3);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());

        // Player1 получает преимущество
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());

        // Player2 возвращает счёт
        match.addPoint(player2);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());

        // Player2 получает преимущество
        match.addPoint(player2);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player2).getGameScore());

        // Player2 выигрывает гейм
        match.addPoint(player2);
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player2).getSetScore());
    }

    @Test
    void testWinFromForty() {
        // Player1: 40-0
        winPoints(player1, 3);
        assertEquals(GameScoreValue.FORTY, match.getScore(player1).getGameScore());

        // Player1 выигрывает гейм
        match.addPoint(player1);
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    @Test
    void testWinAfterOpponentAdvantage() {
        // Доводим до преимущества player2
        winPoints(player1, 3);
        winPoints(player2, 4);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player2).getGameScore());

        // Player1 возвращает преимущество
        match.addPoint(player1);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());

        // Player1 получает преимущество
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());

        // Player1 выигрывает гейм
        match.addPoint(player1);
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    @Test
    void testMatchWithDifferentSetScores() {
        // Player1 выигрывает первый сет 7-5
        winSet(player1, 7, player2, 5);
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());

        // Player2 выигрывает второй сет 6-4
        winSet(player2, 6, player1, 4);
        assertEquals(MatchScoreValue.ONE, match.getScore(player2).getMatchScore());

        // Player1 выигрывает третий сет 7-6
        winGames(player1, 6);
        winGames(player2, 6);
        winPoints(player1, 4); // Тай-брейк

        assertEquals(MatchScoreValue.MATCH, match.getScore(player1).getMatchScore());
    }

    @Test
    void testGameScoreProgression() {
        // Player1 выигрывает 1 очко
        match.addPoint(player1);
        assertEquals(GameScoreValue.FIFTEEN, match.getScore(player1).getGameScore());

        // Player1 выигрывает 2 очка
        match.addPoint(player1);
        assertEquals(GameScoreValue.THIRTY, match.getScore(player1).getGameScore());

        // Player1 выигрывает 3 очка
        match.addPoint(player1);
        assertEquals(GameScoreValue.FORTY, match.getScore(player1).getGameScore());

        // Player1 выигрывает гейм
        match.addPoint(player1);
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
    }

    @Test
    void testSetScoreProgression() {
        // Player1 выигрывает 1 гейм
        winPoints(player1, 4);
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());

        // Player1 выигрывает 2 гейма
        winPoints(player1, 4);
        assertEquals(SetScoreValue.TWO, match.getScore(player1).getSetScore());
    }

    @Test
    void testImmediateWinAfterAdvantage() {
        // Доводим до преимущества player1
        winPoints(player1, 3);
        winPoints(player2, 3);
        match.addPoint(player1); // advantage

        // Player1 выигрывает сразу после получения преимущества
        match.addPoint(player1);
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    @Test
    void testNoWinAt6_6() {
        winGames(player1, 5);
        winGames(player2, 5);
        winGames(player1, 1);
        winGames(player2, 1);

        assertFalse(match.isGameOver());
        assertEquals(SetScoreValue.SIX, match.getScore(player1).getSetScore());
        assertEquals(SetScoreValue.SIX, match.getScore(player2).getSetScore());
    }

    // Вспомогательные методы
    private void winPoints(Player player, int points) {
        for (int i = 0; i < points; i++) {
            match.addPoint(player);
        }
    }

    private void winGames(Player player, int games) {
        for (int i = 0; i < games; i++) {
            winPoints(player, 4);
        }
    }

    private void winSet(Player winner, int winnerGames, Player loser, int loserGames) {
        winGames(loser, loserGames);
        winGames(winner, winnerGames);
    }
}
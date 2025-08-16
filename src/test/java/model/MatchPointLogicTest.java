package model;

import dev.chearcode.model.*;
import dev.chearcode.model.score.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchPointLogicTest {
    private Match match;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        match = new Match(player1, player2);
    }

    // Ветка 1: LOVE -> FIFTEEN
    @Test
    void testLoveToFifteen() {
        match.addPoint(player1);
        assertEquals(GameScoreValue.FIFTEEN, match.getScore(player1).getGameScore());
    }

    // Ветка 2: FIFTEEN -> THIRTY
    @Test
    void testFifteenToThirty() {
        winPoints(player1, 1);
        match.addPoint(player1);
        assertEquals(GameScoreValue.THIRTY, match.getScore(player1).getGameScore());
    }

    // Ветка 3: THIRTY -> FORTY
    @Test
    void testThirtyToForty() {
        winPoints(player1, 2);
        match.addPoint(player1);
        assertEquals(GameScoreValue.FORTY, match.getScore(player1).getGameScore());
    }

    // Ветка 4: FORTY -> GAME (оппонент < FORTY)
    @Test
    void testFortyToGameWhenOpponentBelowForty() {
        winPoints(player1, 3);
        match.addPoint(player1);
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    // Ветка 5: FORTY -> ADVANTAGE (оппонент FORTY)
    @Test
    void testFortyToAdvantageWhenOpponentForty() {
        winPoints(player1, 3);
        winPoints(player2, 3); // 40-40
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());
    }

    // Ветка 6: DEUCE -> ADVANTAGE
    @Test
    void testDeuceToAdvantage() {
        winPoints(player1, 3);
        winPoints(player2, 3); // 40-40 -> Deuce
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());
    }

    // Ветка 7: DEUCE -> Reset Advantage
    @Test
    void testDeuceResetsOpponentAdvantage() {
        // Создаем ситуацию: player2 имеет преимущество
        winPoints(player1, 3);
        winPoints(player2, 4); // Player2 advantage

        match.addPoint(player1); // Возвращаем в deuce
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());
    }

    // Ветка 8: ADVANTAGE -> GAME
    @Test
    void testAdvantageToGame() {
        winPoints(player1, 3);
        winPoints(player2, 3);
        winPoints(player1, 1);
        winPoints(player2, 1); // 40-40
        match.addPoint(player1); // Player1 имеет преимущество
        match.addPoint(player1); // Выигрыш гейма
        assertEquals(GameScoreValue.LOVE, match.getScore(player1).getGameScore());
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    // Ветка 9: FORTY -> DEUCE (особый случай)
    @Test
    void testFortyToDeuceSpecialCase() {
        winPoints(player1, 3);
        winPoints(player2, 2); // 40-30
        match.addPoint(player2); // 40-40 -> Deuce
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());
        assertEquals(GameScoreValue.DEUCE, match.getScore(player2).getGameScore());
    }

    // Ветка 10: Игнорирование очков после завершения матча
    @Test
    void testIgnorePointsAfterMatchOver() {
        winSet(player1, 6, player2, 0);
        winSet(player1, 6, player2, 0);

        GameScoreValue initial = match.getScore(player1).getGameScore();
        match.addPoint(player1);
        assertEquals(initial, match.getScore(player1).getGameScore());
    }

    // Ветка 11: Переход сета при 6-0
    @Test
    void testSetWinAt6_0() {
        winGames(player1, 6);
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
    }

    // Ветка 12: Переход сета при 6-4
    @Test
    void testSetWinAt6_4() {
        winGames(player2, 4);
        winGames(player1, 6);
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
    }

    // Ветка 13: Переход сета при 7-5
    @Test
    void testSetWinAt7_5() {
        winGames(player2, 5);
        winGames(player1, 7);
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
    }

    // Ветка 14: Тай-брейк 7-6
    @Test
    void testTieBreakAt7_6() {
        winGames(player1, 5);
        winGames(player2, 5);
        winGames(player1, 1);
        winGames(player2, 1); // 6-6

        // Player1 выигрывает тай-брейк
        winPoints(player1, 4);
        assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
        assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
    }

    // Ветка 15: Не завершенный сет при 5-5
    @Test
    void testUnfinishedSetAt5_5() {
        winGames(player1, 5);
        winGames(player2, 5);
        assertEquals(SetScoreValue.FIVE, match.getScore(player1).getSetScore());
        assertEquals(SetScoreValue.FIVE, match.getScore(player2).getSetScore());
        assertEquals(MatchScoreValue.ZERO, match.getScore(player1).getMatchScore());
    }

    // Ветка 16: Смена преимущества несколько раз
    @Test
    void testMultipleAdvantageSwitches() {
        // Доводим до deuce
        winPoints(player1, 3);
        winPoints(player2, 3);

        // Player1 advantage
        match.addPoint(player1);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player1).getGameScore());

        // Player2 возвращает
        match.addPoint(player2);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());

        // Player2 advantage
        match.addPoint(player2);
        assertEquals(GameScoreValue.ADVANTAGE, match.getScore(player2).getGameScore());

        // Player1 возвращает
        match.addPoint(player1);
        assertEquals(GameScoreValue.DEUCE, match.getScore(player1).getGameScore());

        // Player1 снова advantage и выигрывает
        match.addPoint(player1);
        match.addPoint(player1);
        assertEquals(SetScoreValue.ONE, match.getScore(player1).getSetScore());
    }

    // Ветка 17: Выигрыш матча после двух сетов
    @Test
    void testMatchWinAfterTwoSets() {
        winSet(player1, 6, player2, 4);
        winSet(player1, 7, player2, 5);
        assertEquals(MatchScoreValue.MATCH, match.getScore(player1).getMatchScore());
        assertTrue(match.isGameOver());
    }

    // Ветка 18: Невалидное состояние счета
    @Test
    void testInvalidScoreState() {
        // Создаем невалидное состояние
        PlayerScore score = match.getScore(player1);
        score.setGameScore(GameScoreValue.GAME); // Пропускаем обычный flow

        assertThrows(IllegalArgumentException.class, () -> match.addPoint(player1));
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
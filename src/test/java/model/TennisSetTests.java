package model;

import dev.chearcode.entity.Player;
import dev.chearcode.model.TennisLevel;
import dev.chearcode.model.TennisSet;
import dev.chearcode.model.TieBreak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TennisSetTests {
    private Player player1;
    private Player player2;
    private TennisSet tennisSet;

    @BeforeEach
    protected void init() {
        player1 = new Player("Player_1");
        player1.setId(1L);
        player2 = new Player("Player_2");
        player2.setId(2L);
        tennisSet = new TennisSet(player1, player2);
    }

    @Test
    public void testFirstGameStarted() {
        assertTrue(tennisSet.getLevels().size() == 1);

        TennisLevel currentGame = tennisSet.getLevels().get(0);
        assertFalse(currentGame.isFinished());
    }

    @Test
    public void testNewGameStarted() {
        wonGames(player1, 1);

        List<TennisLevel> games = tennisSet.getLevels();

        assertEquals(2, games.size());

        TennisLevel currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());
    }

    @Test
    public void testCorrectCalculationOneScore() {
        wonGames(player1, 1);

        List<TennisLevel> games = tennisSet.getLevels();

        assertEquals(2, games.size());

        TennisLevel currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());

        assertEquals("1", tennisSet.getScoreValue(player1));
        assertEquals("0", tennisSet.getScoreValue(player2));
    }

    @Test
    public void testCorrectCalculationScoresInNewGame() {
        addPoints(player2, 3);
        wonCurrentGame(player1);

        List<TennisLevel> games = tennisSet.getLevels();
        assertEquals(2, games.size());

        TennisLevel currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());

        assertEquals("1", tennisSet.getScoreValue(player1));
        assertEquals("0", tennisSet.getScoreValue(player2));

        assertEquals("0", currentGame.getScoreValue(player1));
        assertEquals("0", currentGame.getScoreValue(player2));
    }

    @Test
    public void testSetContinuesWhenBothPlayersReachedMaxPoints() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);

        assertFalse(tennisSet.isFinished());
    }

    @Test
    public void testTieBreakCreated() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);

        assertFalse(tennisSet.isFinished());

        assertEquals("6", tennisSet.getScoreValue(player1));
        assertEquals("6", tennisSet.getScoreValue(player2));

        List<TennisLevel> games = tennisSet.getLevels();
        TennisLevel game = games.get(games.size() - 1);
        assertInstanceOf(TieBreak.class, game);
    }

    @Test
    public void testSetFinishedWithTieBreak() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);
        wonGames(player1, 1);

        assertEquals("7", tennisSet.getScoreValue(player1));
        assertEquals("6", tennisSet.getScoreValue(player2));

        assertTrue(tennisSet.isFinished());
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPoint() {
        wonGames(player1, 6);

        assertTrue(tennisSet.isFinished());

        assertThrows(IllegalStateException.class, () -> tennisSet.pointWonBy(player1));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointInAdditionalGame() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);
        wonGames(player1, 2);

        assertTrue(tennisSet.isFinished());

        assertThrows(IllegalStateException.class, () -> tennisSet.pointWonBy(player1));
    }

    @Test
    public void testRegularVictoryWithoutTieBreak_6_4() {
        wonGames(player1, 4);
        wonGames(player2, 4);
        wonGames(player1, 2);

        assertTrue(tennisSet.isFinished());
        assertEquals("6", tennisSet.getScoreValue(player1));
        assertEquals("4", tennisSet.getScoreValue(player2));
        assertEquals(player1, tennisSet.getWinner());
    }

    @Test
    public void testVictory75Scenario() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        assertFalse(tennisSet.isFinished());

        wonGames(player1, 1);
        assertTrue(tennisSet.isFinished());
        assertEquals("7", tennisSet.getScoreValue(player1));
        assertEquals("5", tennisSet.getScoreValue(player2));
        assertEquals(player1, tennisSet.getWinner());
    }

    @Test
    public void testGetWinnerBeforeFinishThrows() {
        assertThrows(IllegalStateException.class, () -> tennisSet.getWinner());
    }

    private void wonCurrentGame(Player player) {
        List<TennisLevel> games = tennisSet.getLevels();
        TennisLevel game = games.get(games.size() - 1);
        while (!game.isFinished()) {
            tennisSet.pointWonBy(player);
        }
    }

    private void addPoints(Player player, int count) {
        while (count-- > 0) {
            tennisSet.pointWonBy(player);
        }
    }

    private void wonGames(Player player, int count) {
        while (count-- > 0) {
            wonCurrentGame(player);
        }
    }
}

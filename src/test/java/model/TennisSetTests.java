package model;

import dev.chearcode.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TennisSetTests {
    private final Player player1 = new Player(UUID.randomUUID(), "Player_1");
    private final Player player2 = new Player(UUID.randomUUID(), "Player_2");

    private TennisSet tennisSet;

    @BeforeEach
    protected void createNewSet() {
        this.tennisSet = new TennisSet(player1, player2);
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

package model;

import dev.chearcode.model.TennisLevel;
import dev.chearcode.entity.Player;
import dev.chearcode.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    private static final String FIFTEEN = "15";
    private static final String THIRTY = "30";
    private static final String FORTY = "40";
    private static final String ADVANTAGE = "AD";
    private static final String GAME = "GAME";
    private static final String LOVE = "0";
    private Player player1;
    private Player player2;

    private TennisLevel game;

    @BeforeEach
    protected void init() {
        player1 = new Player("Player_1");
        player1.setId(UUID.randomUUID());
        player2 = new Player("Player_2");
        player2.setId(UUID.randomUUID());
        game = new Game(player1, player2);
    }

    @Test
    public void testCorrectCalculationOfPoints() {
        addPoints(player1, 3);
        addPoints(player2, 1);

        assertFalse(game.isFinished());

        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FIFTEEN, game.getScoreValue(player2));
    }

    @Test
    public void testGameContinuesWithABigDifference() {
        addPoints(player1, 3);

        assertFalse(game.isFinished());
    }

    @Test
    public void testCleanVictory() {
        addPoints(player1, 4);

        assertTrue(game.isFinished());
    }

    @Test
    public void testGameFinishWithMinDifference() {
        addPoints(player1, 3);
        addPoints(player2, 2);

        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(THIRTY, game.getScoreValue(player2));

        addPoints(player1, 1);
        assertTrue(game.isFinished());
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testGameContinuesWithTakenAdvantage() {
        addPoints(player1, 3);
        addPoints(player2, 3);

        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));

        addPoints(player1, 1);

        assertEquals(ADVANTAGE, game.getScoreValue(player1));
        assertFalse(game.isFinished());
    }

    @Test
    public void testCorrectTakesAndLostAdvantage() {
        addPoints(player1, 3);
        addPoints(player2, 3);

        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));

        addPoints(player1, 1);
        assertEquals(ADVANTAGE, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));
        assertFalse(game.isFinished());

        addPoints(player2, 1);
        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));
        assertFalse(game.isFinished());

        addPoints(player2, 1);
        assertEquals(ADVANTAGE, game.getScoreValue(player2));
        assertEquals(FORTY, game.getScoreValue(player1));
        assertFalse(game.isFinished());

        addPoints(player1, 1);
        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));

        addPoints(player1, 2);
        assertTrue(game.isFinished());
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPoint() {
        addPoints(player1, 4);

        assertTrue(game.isFinished());

        assertThrows(IllegalStateException.class, () -> game.pointWonBy(player1));
    }

    @Test
    public void testGetWinnerBeforeFinishThrows() {
        assertThrows(IllegalStateException.class, () -> game.getWinner());
    }

    @Test
    public void testCleanVictoryFromDeuce() {
        addPoints(player1, 3);
        addPoints(player2, 3);
        assertEquals(FORTY, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));

        addPoints(player1, 1);
        assertEquals(ADVANTAGE, game.getScoreValue(player1));
        assertFalse(game.isFinished());

        addPoints(player1, 1);
        assertTrue(game.isFinished());

        assertEquals(GAME, game.getScoreValue(player1));
        assertEquals(FORTY, game.getScoreValue(player2));
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testScoreValueAfterFinishCleanVictory() {
        addPoints(player2, 4);
        assertTrue(game.isFinished());
        assertEquals(GAME, game.getScoreValue(player2));

        assertEquals(LOVE, game.getScoreValue(player1));
        assertEquals(player2, game.getWinner());
    }

    private void addPoints(Player player, int count) {
        while (count-- > 0) {
            game.pointWonBy(player);
        }
    }
}

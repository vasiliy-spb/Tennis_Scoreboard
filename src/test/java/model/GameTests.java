package model;

import dev.chearcode.model.TennisLevel;
import dev.chearcode.model.Player;
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
    private final Player player1 = new Player(UUID.randomUUID(), "Player_1");
    private final Player player2 = new Player(UUID.randomUUID(), "Player_2");

    private TennisLevel game;

    @BeforeEach
    protected void createNewGame() {
        this.game = new Game(player1, player2);
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

    private void addPoints(Player player, int count) {
        while (count-- > 0) {
            game.pointWonBy(player);
        }
    }
}

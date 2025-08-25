package model;

import dev.chearcode.model.TennisLevel;
import dev.chearcode.model.Player;
import dev.chearcode.model.TieBreak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TieBreakTests {
    private final Player player1 = new Player(UUID.randomUUID(), "Player_1");
    private final Player player2 = new Player(UUID.randomUUID(), "Player_2");

    private TennisLevel game;

    @BeforeEach
    protected void createNewGame() {
        this.game = new TieBreak(player1, player2);
    }

    @Test
    public void testGameContinuesWithBigDifference() {
        addPoints(player1, 6);

        assertFalse(game.isFinished());
    }

    @Test
    public void testGameContinuesWithBothPlayersReachMinNecessaryPoints() {
        addPoints(player1, 6);
        addPoints(player2, 6);

        assertFalse(game.isFinished());
    }

    @Test
    public void testGameContinuesWithMaxPointsButMinDifference() {
        addPoints(player1, 6);
        addPoints(player2, 7);

        assertFalse(game.isFinished());
    }

    @Test
    public void testGameFinishedWithoutAdditionalPoint() {
        addPoints(player1, 5);
        addPoints(player2, 7);

        assertTrue(game.isFinished());
        assertEquals(player2, game.getWinner());
    }

    @Test
    public void testGameFinishedWithAdditionalPoint() {
        addPoints(player1, 6);
        addPoints(player2, 8);

        assertTrue(game.isFinished());
        assertEquals(player2, game.getWinner());
    }

    @Test
    public void testCleanVictory() {
        addPoints(player1, 7);

        assertTrue(game.isFinished());
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testGameContinuesUntilMinDiffHasBeenReached() {
        for (int i = 0; i < 12; i++) {
            addPoints(player1, 1);
            addPoints(player2, 1);
            assertFalse(game.isFinished());
        }

        addPoints(player1, 2);
        assertTrue(game.isFinished());

        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPoint() {
        addPoints(player1, 7);

        assertTrue(game.isFinished());

        assertThrows(IllegalStateException.class, () -> game.pointWonBy(player1));
    }


    private void addPoints(Player player, int count) {
        while (count-- > 0) {
            game.pointWonBy(player);
        }
    }
}

package model;

import dev.chearcode.model.Match;
import dev.chearcode.model.Player;
import dev.chearcode.model.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTests {
    private final Player player1 = new Player(UUID.randomUUID(), "Player_1");
    private final Player player2 = new Player(UUID.randomUUID(), "Player_2");

    private Match match;

    @BeforeEach
    protected void createNewSet() {
        this.match = new Match(player1, player2);
    }

    @Test
    public void testFirstSetStarted() {
        assertTrue(match.getSets().size() == 1);

        Set currentSet = match.getSets().get(0);
        assertFalse(currentSet.isFinished());
    }

    @Test
    public void testNewGameStarted() {
        wonCurrentSet(player2);

        List<Set> sets = match.getSets();
        assertTrue(sets.size() == 2);

        Set currentSet = sets.get(sets.size() - 1);
        assertFalse(currentSet.isFinished());
    }

    @Test
    public void testCorrectCalculationOneScore() {
        wonCurrentSet(player2);

        List<Set> sets = match.getSets();
        assertTrue(sets.size() == 2);

        Set currentSet = sets.get(sets.size() - 1);
        assertFalse(currentSet.isFinished());

        assertEquals(0, match.getScore(player1));
        assertEquals(1, match.getScore(player2));
    }

    @Test
    public void testCorrectCalculationScoresInNewSet() {
        wonCurrentSet(player2);

        List<Set> sets = match.getSets();
        assertTrue(sets.size() == 2);

        Set currentSet = sets.get(sets.size() - 1);
        assertFalse(currentSet.isFinished());

        assertEquals(0, match.getScore(player1));
        assertEquals(1, match.getScore(player2));

        assertEquals(0, currentSet.getScore(player1));
        assertEquals(0, currentSet.getScore(player2));
    }

    @Test
    public void testMatchContinuesWhenBothPlayersReachedMaxPoints() {
        wonSets(player1, 1);
        wonSets(player2, 1);

        assertFalse(match.isFinished());
    }

    @Test
    public void testMatchFinishedWithMinDiff() {
        wonSets(player2, 1);
        wonSets(player1, 2);

        assertTrue(match.isFinished());
        assertEquals(2, match.getScore(player1));
        assertEquals(1, match.getScore(player2));
    }

    @Test
    public void testMatchFinishedWithCleanVictory() {
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals(0, match.getScore(player1));
        assertEquals(2, match.getScore(player2));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointAfterMatchFinishedWithCleanVictory() {
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals(0, match.getScore(player1));
        assertEquals(2, match.getScore(player2));

        assertThrows(IllegalStateException.class, () -> match.pointWonBy(player1));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointAfterMatchFinished() {
        wonSets(player1, 1);
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals(1, match.getScore(player1));
        assertEquals(2, match.getScore(player2));

        assertThrows(IllegalStateException.class, () -> match.pointWonBy(player1));
    }

    private void wonSets(Player player, int count) {
        while (count-- > 0) {
            wonCurrentSet(player);
        }
    }

    private void wonCurrentSet(Player player) {
        List<Set> sets = match.getSets();
        Set set = sets.get(sets.size() - 1);
        while (!set.isFinished()) {
            match.pointWonBy(player);
        }
    }
}

package model;

import dev.chearcode.model.Match;
import dev.chearcode.model.Player;
import dev.chearcode.model.TennisLevel;
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
        assertTrue(match.getLevels().size() == 1);

        TennisLevel currentTennisSet = match.getLevels().get(0);
        assertFalse(currentTennisSet.isFinished());
    }

    @Test
    public void testNewGameStarted() {
        wonCurrentSet(player2);

        List<TennisLevel> tennisSets = match.getLevels();
        assertTrue(tennisSets.size() == 2);

        TennisLevel currentTennisSet = tennisSets.get(tennisSets.size() - 1);
        assertFalse(currentTennisSet.isFinished());
    }

    @Test
    public void testCorrectCalculationOneScore() {
        wonCurrentSet(player2);

        List<TennisLevel> tennisSets = match.getLevels();
        assertTrue(tennisSets.size() == 2);

        TennisLevel currentTennisSet = tennisSets.get(tennisSets.size() - 1);
        assertFalse(currentTennisSet.isFinished());

        assertEquals("0", match.getScoreValue(player1));
        assertEquals("1", match.getScoreValue(player2));
    }

    @Test
    public void testCorrectCalculationScoresInNewSet() {
        wonCurrentSet(player2);

        List<TennisLevel> tennisSets = match.getLevels();
        assertTrue(tennisSets.size() == 2);

        TennisLevel currentTennisSet = tennisSets.get(tennisSets.size() - 1);
        assertFalse(currentTennisSet.isFinished());

        assertEquals("0", match.getScoreValue(player1));
        assertEquals("1", match.getScoreValue(player2));

        assertEquals("0", currentTennisSet.getScoreValue(player1));
        assertEquals("0", currentTennisSet.getScoreValue(player2));
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
        assertEquals("2", match.getScoreValue(player1));
        assertEquals("1", match.getScoreValue(player2));
    }

    @Test
    public void testMatchFinishedWithCleanVictory() {
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals("0", match.getScoreValue(player1));
        assertEquals("2", match.getScoreValue(player2));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointAfterMatchFinishedWithCleanVictory() {
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals("0", match.getScoreValue(player1));
        assertEquals("2", match.getScoreValue(player2));

        assertThrows(IllegalStateException.class, () -> match.pointWonBy(player1));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointAfterMatchFinished() {
        wonSets(player1, 1);
        wonSets(player2, 2);

        assertTrue(match.isFinished());
        assertEquals("1", match.getScoreValue(player1));
        assertEquals("2", match.getScoreValue(player2));

        assertThrows(IllegalStateException.class, () -> match.pointWonBy(player1));
    }

    private void wonSets(Player player, int count) {
        while (count-- > 0) {
            wonCurrentSet(player);
        }
    }

    private void wonCurrentSet(Player player) {
        List<TennisLevel> tennisSets = match.getLevels();
        TennisLevel tennisSet = tennisSets.get(tennisSets.size() - 1);
        while (!tennisSet.isFinished()) {
            match.pointWonBy(player);
        }
    }
}

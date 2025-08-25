package model;

import dev.chearcode.entity.Player;
import dev.chearcode.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TennisMatchTests {
    private Player player1;
    private Player player2;
    private TennisMatch match;

    @BeforeEach
    protected void init() {
        player1 = new Player("Player_1");
        player1.setId(UUID.randomUUID());
        player2 = new Player("Player_2");
        player2.setId(UUID.randomUUID());
        match = new TennisMatch(player1, player2);
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

    @Test
    public void testGetWinnerBeforeFinishThrows() {
        assertThrows(IllegalStateException.class, () -> match.getWinner());
    }

    @Test
    public void testMatchWithTieBreakSets() {
        winGamesSimple(match, player1, 5);
        winGamesSimple(match, player2, 5);
        winGamesSimple(match, player1, 1);
        winGamesSimple(match, player2, 1);

        TennisSet firstSet = (TennisSet) match.getLevels().get(0);
        List<TennisLevel> games = firstSet.getLevels();
        TennisLevel tennisLevel = games.get(games.size() - 1);
        assertTrue(tennisLevel instanceof TieBreak);

        while (!tennisLevel.isFinished()) {
            match.pointWonBy(player1);
        }
        assertTrue(firstSet.isFinished());
        assertEquals("7", firstSet.getScoreValue(player1));
        assertEquals("6", firstSet.getScoreValue(player2));

        winGamesSimple(match, player1, 4);
        winGamesSimple(match, player2, 6);
        TennisSet secondSet = (TennisSet) match.getLevels().get(1);
        assertTrue(secondSet.isFinished());
        assertEquals("4", secondSet.getScoreValue(player1));
        assertEquals("6", secondSet.getScoreValue(player2));

        winGamesSimple(match, player2, 2);
        winGamesSimple(match, player1, 6);
        assertTrue(match.isFinished());
        assertEquals("2", match.getScoreValue(player1));
        assertEquals("1", match.getScoreValue(player2));
        assertEquals(player1, match.getWinner());
    }

    private void winStraightGame(TennisMatch match, Player player) {
        for (int i = 0; i < 4; i++) {
            match.pointWonBy(player);
        }
    }

    private void winGamesSimple(TennisMatch match, Player player, int count) {
        while (count-- > 0) {
            winStraightGame(match, player);
        }
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

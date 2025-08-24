package model;

import dev.chearcode.model.Game;
import dev.chearcode.model.Player;
import dev.chearcode.model.Set;
import dev.chearcode.model.TieBreak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SetTests {
    private final Player player1 = new Player(UUID.randomUUID(), "Player_1");
    private final Player player2 = new Player(UUID.randomUUID(), "Player_2");

    private Set set;

    @BeforeEach
    protected void createNewSet() {
        this.set = new Set(player1, player2);
    }

    @Test
    public void testFirstGameStarted() {
        assertTrue(set.getGames().size() == 1);

        Game currentGame = set.getGames().get(0);
        assertFalse(currentGame.isFinished());
    }

    @Test
    public void testNewGameStarted() {
        wonGames(player1, 1);

        List<Game> games = set.getGames();

        assertEquals(2, games.size());

        Game currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());
    }

    @Test
    public void testCorrectCalculationOneScore() {
        wonGames(player1, 1);

        List<Game> games = set.getGames();

        assertEquals(2, games.size());

        Game currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());

        assertEquals(1, set.getScore(player1));
        assertEquals(0, set.getScore(player2));
    }

    @Test
    public void testCorrectCalculationScoresInNewGame() {
        addPoints(player2, 3);
        wonCurrentGame(player1);

        List<Game> games = set.getGames();
        assertEquals(2, games.size());

        Game currentGame = games.get(games.size() - 1);
        assertFalse(currentGame.isFinished());

        assertEquals(1, set.getScore(player1));
        assertEquals(0, set.getScore(player2));

        assertEquals(0, currentGame.getScore(player1));
        assertEquals(0, currentGame.getScore(player2));
    }

    @Test
    public void testSetContinuesWhenBothPlayersReachedMaxPoints() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);

        assertFalse(set.isFinished());
    }

    @Test
    public void testTieBreakCreated() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);

        assertFalse(set.isFinished());

        assertEquals(6, set.getScore(player1));
        assertEquals(6, set.getScore(player2));

        List<Game> games = set.getGames();
        Game game = games.get(games.size() - 1);
        assertInstanceOf(TieBreak.class, game);
    }

    @Test
    public void testSetFinishedWithTieBreak() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);
        wonGames(player1, 1);

        assertEquals(7, set.getScore(player1));
        assertEquals(6, set.getScore(player2));

        assertTrue(set.isFinished());
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPoint() {
        wonGames(player1, 6);

        assertTrue(set.isFinished());

        assertThrows(IllegalStateException.class, () -> set.pointWonBy(player1));
    }

    @Test
    public void testTrowExceptionWithTryingToAddExtraPointInAdditionalGame() {
        wonGames(player1, 5);
        wonGames(player2, 5);
        wonGames(player1, 1);
        wonGames(player2, 1);
        wonGames(player1, 2);

        assertTrue(set.isFinished());

        assertThrows(IllegalStateException.class, () -> set.pointWonBy(player1));
    }

    private void wonCurrentGame(Player player) {
        List<Game> games = set.getGames();
        Game game = games.get(games.size() - 1);
        while (!game.isFinished()) {
            set.pointWonBy(player);
        }
    }

    private void addPoints(Player player, int count) {
        while (count-- > 0) {
            set.pointWonBy(player);
        }
    }

    private void wonGames(Player player, int count) {
        while (count-- > 0) {
            wonCurrentGame(player);
        }
    }
}

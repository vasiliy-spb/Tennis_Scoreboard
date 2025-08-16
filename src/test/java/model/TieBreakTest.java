package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import dev.chearcode.model.Match;
import dev.chearcode.model.Player;
import dev.chearcode.model.score.MatchScoreValue;
import dev.chearcode.model.score.SetScoreValue;
import java.util.UUID;

public class TieBreakTest {

    @Test
    public void testSetWinAfterSixSix() {
        // Установка начального счета
        Player player1 = new Player(UUID.randomUUID(), "Player 1");
        Player player2 = new Player(UUID.randomUUID(), "Player 2");
        Match match = new Match(UUID.randomUUID(), player1, player2);

        // Устанавливаем счет в сете 6-6
        // Симулируем 12 геймов
        for (int i = 0; i < 6; i++) {
            // Player 1 выигрывает гейм
            // В реальном коде это было бы 4 вызова addPoint для каждого гейма
            match.getScore(player1).setSetScore(SetScoreValue.values()[match.getScore(player1).getSetScore().ordinal() + 1]);
            // Player 2 выигрывает гейм
            match.getScore(player2).setSetScore(SetScoreValue.values()[match.getScore(player2).getSetScore().ordinal() + 1]);
        }

        // Проверяем, что текущий счет в сете 6-6
        Assertions.assertEquals(SetScoreValue.SIX, match.getScore(player1).getSetScore());
        Assertions.assertEquals(SetScoreValue.SIX, match.getScore(player2).getSetScore());

        // Player 1 выигрывает следующее очко (что в вашем коде означает победу в гейме)
        // В вашем коде это симулируется одним вызовом addPoint, который, по-видимому,
        // содержит всю логику.
        // Я сделаю 4 вызова addPoint, чтобы симулировать выигрыш гейма.
        for (int i = 0; i < 4; i++) {
            match.addPoint(player1);
        }

        // Проверяем, что счет в сете Player 1 сбросился
        Assertions.assertEquals(SetScoreValue.ZERO, match.getScore(player1).getSetScore());
        // Проверяем, что Player 1 получил очко в матче
        Assertions.assertEquals(MatchScoreValue.ONE, match.getScore(player1).getMatchScore());
        // Проверяем, что счет в сете Player 2 сбросился
        Assertions.assertEquals(SetScoreValue.ZERO, match.getScore(player2).getSetScore());
    }
}

package dev.chearcode.model.score;

import java.util.UUID;

public class MatchScore {
    private final UUID matchId;
    private final PlayerScore first;
    private final PlayerScore second;

    public MatchScore(UUID matchId, PlayerScore first, PlayerScore second) {
        this.matchId = matchId;
        this.first = first;
        this.second = second;
    }

    public void resetGameScore(PlayerScore playerScore) {
        playerScore.resetGameScore();
    }

    public void resetSetScore(PlayerScore playerScore) {
        playerScore.resetSetScore();
    }

    public UUID getMatchId() {
        return matchId;
    }

    public PlayerScore getFirst() {
        return first;
    }

    public PlayerScore getSecond() {
        return second;
    }
}

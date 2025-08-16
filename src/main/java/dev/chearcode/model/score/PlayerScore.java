package dev.chearcode.model.score;

public class PlayerScore {
    private GameScoreValue gameScoreValue;
    private SetScoreValue setScoreValue;
    private MatchScoreValue matchScoreValue;

    public PlayerScore() {
        this.gameScoreValue = GameScoreValue.LOVE;
        this.setScoreValue = SetScoreValue.ZERO;
        this.matchScoreValue = MatchScoreValue.ZERO;
    }

    public void resetGameScore() {
        this.gameScoreValue = GameScoreValue.LOVE;
    }

    public void resetSetScore() {
        this.setScoreValue = SetScoreValue.ZERO;
    }

    public GameScoreValue getGameScore() {
        return gameScoreValue;
    }

    public void setGameScore(GameScoreValue gameScoreValue) {
        this.gameScoreValue = gameScoreValue;
    }

    public SetScoreValue getSetScore() {
        return setScoreValue;
    }

    public void setSetScore(SetScoreValue setScoreValue) {
        this.setScoreValue = setScoreValue;
    }

    public MatchScoreValue getMatchScore() {
        return matchScoreValue;
    }

    public void setMatchScore(MatchScoreValue matchScoreValue) {
        this.matchScoreValue = matchScoreValue;
    }
}

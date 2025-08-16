package dev.chearcode.new_model.score;

public class Score {
    private GameScore gameScore;
    private SetScore setScore;
    private MatchScore matchScore;

    public Score() {
        this.gameScore = GameScore.LOVE;
        this.setScore = SetScore.ZERO;
        this.matchScore = MatchScore.ZERO;
    }

    public void resetGameScore() {
        this.gameScore = GameScore.LOVE;
    }

    public void resetSetScore() {
        this.setScore = SetScore.ZERO;
    }

    public GameScore getGameScore() {
        return gameScore;
    }

    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    public SetScore getSetScore() {
        return setScore;
    }

    public void setSetScore(SetScore setScore) {
        this.setScore = setScore;
    }

    public MatchScore getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(MatchScore matchScore) {
        this.matchScore = matchScore;
    }
}

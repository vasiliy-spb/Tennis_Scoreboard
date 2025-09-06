package dev.chearcode.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matches")
public class Match extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_player_id", nullable = false, updatable = false)
    private Player firstPlayer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_player_id", nullable = false, updatable = false)
    private Player secondPlayer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winner_id", nullable = false, updatable = false)
    private Player winner;

    public Match() {
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}

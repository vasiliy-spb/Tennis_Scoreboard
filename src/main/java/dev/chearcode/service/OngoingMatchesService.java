package dev.chearcode.service;

import dev.chearcode.entity.Player;
import dev.chearcode.model.TennisMatch;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private final ConcurrentHashMap<UUID, TennisMatch> matches;

    public OngoingMatchesService() {
        this.matches = new ConcurrentHashMap<>();
    }

    public void addNewMatch(UUID matchId, TennisMatch match) {
        matches.put(matchId, match);
    }

    public void pointWonBy(UUID matchId, Player player) {
        matches.computeIfPresent(matchId, (id, match) ->
        {
            match.pointWonBy(player);
            return match;
        });
    }

    public TennisMatch get(UUID matchId) {
        return matches.get(matchId);
    }

    public void remove(UUID matchId) {
        matches.remove(matchId);
    }
}

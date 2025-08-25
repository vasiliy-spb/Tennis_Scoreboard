package dev.chearcode.service;

import dev.chearcode.model.TennisMatch;
import dev.chearcode.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private final ConcurrentHashMap<UUID, TennisMatch> matches;

    public OngoingMatchesService() {
        this.matches = new ConcurrentHashMap<>();
    }

    public void pointWonBy(UUID matchId, Player player) {
        matches.computeIfPresent(matchId, (id, match) ->
        {
            match.pointWonBy(player);
            return match;
        });
    }

    public void addNewMatch(TennisMatch match) {
        matches.put(match.getId(), match);
    }
}

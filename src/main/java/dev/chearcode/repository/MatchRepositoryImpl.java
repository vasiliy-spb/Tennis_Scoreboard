package dev.chearcode.repository;

import dev.chearcode.config.HibernateManager;
import dev.chearcode.entity.Match;

import java.util.List;
import java.util.Optional;

public class MatchRepositoryImpl extends BaseRepository<Match> implements MatchRepository {
    private static final String FIND_ALL_HQL = "FROM Match";
    private static final String FIND_BY_PLAYERS_HQL = FIND_ALL_HQL + " WHERE firstPlayer.name = :firstPlayerName AND secondPlayer.name = :secondPlayerName";
    private static final String FIND_ALL_BY_PLAYER_HQL = FIND_ALL_HQL + " WHERE firstPlayer.name = :name OR secondPlayer.name = :name";

    public MatchRepositoryImpl() {
        super(FIND_ALL_HQL, Match.class);
    }

    @Override
    public Optional<Match> findByPlayers(String firstPlayerName, String secondPlayerName) {
        return HibernateManager.getSession()
                .createQuery(FIND_BY_PLAYERS_HQL, Match.class)
                .setParameter("firstPlayerName", firstPlayerName)
                .setParameter("secondPlayerName", secondPlayerName)
                .uniqueResultOptional();
    }

    @Override
    public List<Match> findAllByPlayer(String name, int limit, int offset) {
        return HibernateManager.getSession()
                .createQuery(FIND_ALL_BY_PLAYER_HQL, Match.class)
                .setParameter("name", name)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }
}

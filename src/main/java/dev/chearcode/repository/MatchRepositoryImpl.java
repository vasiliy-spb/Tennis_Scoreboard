package dev.chearcode.repository;

import dev.chearcode.config.HibernateManager;
import dev.chearcode.entity.Match;

import java.util.List;

public class MatchRepositoryImpl extends BaseRepository<Match> implements MatchRepository {
    private static final String FIND_ALL_HQL = """
                SELECT DISTINCT m
                FROM Match m
                LEFT JOIN FETCH m.firstPlayer
                LEFT JOIN FETCH m.secondPlayer
                LEFT JOIN FETCH m.winner
            """;
    private static final String FILTER_BY_NAME_HQL = " WHERE LOWER(m.firstPlayer.name) LIKE LOWER(:name) OR LOWER(m.secondPlayer.name) LIKE LOWER(:name)";
    private static final String FIND_ALL_BY_PLAYER_HQL = FIND_ALL_HQL + FILTER_BY_NAME_HQL;
    private static final String COUNT_ALL_HQL = "SELECT COUNT(DISTINCT m) FROM Match m";
    private static final String COUNT_ALL_BY_PLAYER_HQL = COUNT_ALL_HQL + FILTER_BY_NAME_HQL;

    public MatchRepositoryImpl() {
        super(FIND_ALL_HQL, Match.class);
    }

    @Override
    public List<Match> findAllByPlayer(String name, int limit, int offset) {
        return HibernateManager.getSession()
                .createQuery(FIND_ALL_BY_PLAYER_HQL, Match.class)
                .setParameter("name", "%" + name + "%")
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public long countAll() {
        return HibernateManager.getSession()
                .createQuery(COUNT_ALL_HQL, Long.class)
                .uniqueResult();
    }

    @Override
    public long countAllByPlayer(String name) {
        return HibernateManager.getSession()
                .createQuery(COUNT_ALL_BY_PLAYER_HQL, Long.class)
                .setParameter("name", "%" + name + "%")
                .uniqueResult();
    }
}

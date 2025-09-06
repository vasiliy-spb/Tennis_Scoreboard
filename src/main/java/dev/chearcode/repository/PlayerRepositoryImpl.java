package dev.chearcode.repository;

import dev.chearcode.config.HibernateManager;
import dev.chearcode.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerRepositoryImpl extends BaseRepository<Player> implements PlayerRepository {
    private static final String FIND_ALL_HQL = "FROM Player";
    private static final String FIND_BY_NAME_HQL = FIND_ALL_HQL + " WHERE name = :name";
    private static final String FIND_BY_ID_HQL = FIND_ALL_HQL + " WHERE id = :id";

    public PlayerRepositoryImpl() {
        super(FIND_ALL_HQL, Player.class);
    }

    @Override
    public Optional<Player> findByName(String name) {
        return HibernateManager.getSession()
                .createQuery(FIND_BY_NAME_HQL, Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return HibernateManager.getSession()
                .createQuery(FIND_BY_ID_HQL, Player.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }
}

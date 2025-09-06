package dev.chearcode.repository;

import dev.chearcode.config.HibernateManager;
import dev.chearcode.entity.BaseEntity;

import java.util.List;
import java.util.UUID;

public abstract class BaseRepository<E extends BaseEntity> implements EntityRepository<E> {
    private final String findAllHql;
    private final Class<E> eClass;

    protected BaseRepository(String findAllHql, Class<E> eClass) {
        this.findAllHql = findAllHql;
        this.eClass = eClass;
    }

    @Override
    public List<E> findAll(int limit, int offset) {
        return HibernateManager.getSession()
                .createQuery(findAllHql, eClass)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public UUID save(E entity) {
        HibernateManager.getSession()
                .persist(entity);
        return entity.getId();
    }
}

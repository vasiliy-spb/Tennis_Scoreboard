package dev.chearcode.repository;

import java.util.List;

public interface EntityRepository<E> {
    List<E> findAll(int limit, int offset);

    Long save(E entity);
}

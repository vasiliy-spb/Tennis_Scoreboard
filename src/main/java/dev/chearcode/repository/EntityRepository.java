package dev.chearcode.repository;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<E> {
    List<E> findAll(int limit, int offset);

    UUID save(E entity);
}

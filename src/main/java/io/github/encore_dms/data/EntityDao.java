package io.github.encore_dms.data;

import io.github.encore_dms.domain.Entity;

import java.util.stream.Stream;

public interface EntityDao {

    <T extends Entity> Stream<T> getAll(Class<T> entityType);

    <T extends Entity> Stream<T> getAll(String entityName, Class<T> entityType);

    void persist(Entity entity);

    <T extends Entity> Query<T> createQuery(String qlString, Class<T> resultClass);

    <T extends Entity> Query<T> createNamedQuery(String name, Class<T> resultClass);

}

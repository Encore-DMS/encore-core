package io.github.encore_dms.data;

import io.github.encore_dms.domain.Entity;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.stream.Stream;

public class DefaultEntityDao implements EntityDao {

    private final EntityManager entityManager;

    public DefaultEntityDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public <T extends Entity> Stream<T> getAll(Class<T> entityType) {
        return getAll(entityType.getSimpleName(), entityType);
    }

    @Override
    public <T extends Entity> Stream<T> getAll(String entityName, Class<T> entityType) {
        return createQuery("SELECT e FROM " + entityName + " e", entityType).stream();
    }

    @Override
    public void persist(Entity entity) {
        entityManager.persist(entity);
    }

    @Override
    public <T extends Entity> Query<T> createQuery(String qlString, Class<T> resultClass) {
        Session session = entityManager.unwrap(Session.class);
        return new Query<>(session.createQuery(qlString, resultClass));
    }

    @Override
    public <T extends Entity> Query<T> createNamedQuery(String name, Class<T> resultClass) {
        Session session = entityManager.unwrap(Session.class);
        return new Query<>(session.createNamedQuery(name, resultClass));
    }

}

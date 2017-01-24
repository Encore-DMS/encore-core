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
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("SELECT e FROM " + entityType.getSimpleName() + " e", entityType).stream();
    }

    @Override
    public void persist(Entity entity) {
        entityManager.persist(entity);
    }
}

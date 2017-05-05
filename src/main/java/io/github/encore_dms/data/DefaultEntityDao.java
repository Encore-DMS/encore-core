package io.github.encore_dms.data;

import io.github.encore_dms.domain.Entity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Map;
import java.util.stream.Stream;

public class DefaultEntityDao implements EntityDao {

    private final Session session;

    public DefaultEntityDao(Session session) {
        this.session = session;
    }

    @Override
    public <T extends Entity> Stream<T> getAll(Class<T> entityType) {
        return getAll(entityType.getSimpleName(), entityType);
    }

    @Override
    public <T extends Entity> Stream<T> getAll(String entityName, Class<T> entityType) {
        return query("SELECT e FROM " + entityName + " e", entityType);
    }

    @Override
    public void persist(Entity entity) {
        session.persist(entity);
    }

    @Override
    public <T extends Entity> Stream<T> query(String qlString, Class<T> resultClass) {
        return session.createQuery(qlString, resultClass).stream();
    }

    @Override
    public <T extends Entity> Stream<T> namedQuery(String name, Map<String, Object> parameters, Class<T> resultClass) {
        Query<T> query = session.createNamedQuery(name, resultClass);
        for (Map.Entry<String, Object> p : parameters.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
        return query.stream();
    }

}

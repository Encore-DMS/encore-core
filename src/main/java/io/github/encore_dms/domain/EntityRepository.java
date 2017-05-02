package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;
import io.github.encore_dms.data.Query;

import java.util.stream.Stream;

public interface EntityRepository {

    Stream<Project> getProjects();

    User getUser(String username);

    void persist(Entity entity);

    <T extends Entity> Query<T> createQuery(String qlString, Class<T> resultClass);

    <T extends Entity> Query<T> createNamedQuery(String name, Class<T> resultClass);

    interface Factory {
        EntityRepository create(EntityDao dao, DataContext context);
    }

}

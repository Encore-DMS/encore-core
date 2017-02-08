package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;

import java.util.stream.Stream;

public interface EntityRepository {

    Stream<Project> getProjects();

    void persist(Entity entity);

    <T extends Entity> Stream<T> query(String qlString, Class<T> resultClass);

    interface Factory {
        EntityRepository create(EntityDao dao, DataContext context);
    }

}

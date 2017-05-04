package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;

import java.util.stream.Stream;

public interface EntityRepository {

    Stream<Project> getProjects();

    User getUserWithUsername(String username);

    void persist(Entity entity);

    interface Factory {
        EntityRepository create(EntityDao dao, DataContext context);
    }

}

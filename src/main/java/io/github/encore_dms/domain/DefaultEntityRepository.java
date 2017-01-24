package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;

import java.util.stream.Stream;

public class DefaultEntityRepository implements EntityRepository {

    private final EntityDao dao;
    private final DataContext context;

    public DefaultEntityRepository(EntityDao dao, DataContext context) {
        this.dao = dao;
        this.context = context;
    }

    @Override
    public Stream<Project> getProjects() {
        return dao.getAll(Project.class);
    }

    @Override
    public void persist(Entity entity) {
        dao.persist(entity);
    }
}

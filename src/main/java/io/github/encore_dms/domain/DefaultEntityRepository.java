package io.github.encore_dms.domain;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;

import java.util.stream.Stream;

public class DefaultEntityRepository implements EntityRepository {

    private final EntityDao dao;
    private final DataContext context;

    @Inject
    public DefaultEntityRepository(@Assisted EntityDao dao, @Assisted DataContext context) {
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

    @Override
    public <T extends Entity> Stream<T> query(String qlString, Class<T> resultClass) {
        return dao.query(qlString, resultClass);
    }
}

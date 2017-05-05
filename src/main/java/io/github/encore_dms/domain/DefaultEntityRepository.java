package io.github.encore_dms.domain;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.data.EntityDao;

import javax.persistence.EntityNotFoundException;
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
    public User getUserWithUsername(String username) {
        return dao.namedQuery("User.findByUsername", ImmutableMap.of("username", username), User.class)
                .findFirst().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void persist(Entity entity) {
        dao.persist(entity);
    }

}

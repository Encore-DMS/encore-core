package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.EntityRepository;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface DataContext extends TransactionManager {

    EntityRepository getRepository();

    DataStoreCoordinator getCoordinator();

    Stream<Project> getProjects();

    Project insertProject(String name, String purpose, ZonedDateTime start, ZonedDateTime end);

    void insertEntity(Entity entity);

    User getAuthenticatedUser();

    interface Factory {
        DataContext create(DataStore primaryDataStore, DataStoreCoordinator coordinator);
    }

}

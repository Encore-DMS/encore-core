package io.github.encore_dms;

import com.physion.ebuilder.expression.ExpressionTree;
import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.EntityRepository;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface DataContext extends TransactionManager {

    <T extends Entity> Stream<T> query(String qlString, Class<T> resultClass);

    <T extends Entity> Stream<T> query(ExpressionTree expressionTree);

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

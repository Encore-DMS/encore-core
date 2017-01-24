package io.github.encore_dms;

import com.google.inject.Inject;
import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.EntityRepository;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.util.TransactionUtilities;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DefaultDataContext implements DataContext {

    private final EntityRepository entityRepository;
    private final DataStoreCoordinator dataStoreCoordinator;
    private final TransactionManager transactionManager;

    @Inject
    DefaultDataContext(DataStore dataStore, DataStoreCoordinator dataStoreCoordinator, EntityRepository.Factory entityRepositoryFactory) {
        this.dataStoreCoordinator = dataStoreCoordinator;
        this.entityRepository = entityRepositoryFactory.create(dataStore.getDao(), this);
        this.transactionManager = dataStore.getTransactionManager();
    }

    @Override
    public EntityRepository getRepository() {
        return this.entityRepository;
    }

    @Override
    public Stream<Project> getProjects() {
        return getRepository().getProjects();
    }

    @Override
    public Project insertProject(String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        return TransactionUtilities.transactionWrapped(this, () -> {
            Project p = new Project(DefaultDataContext.this, getAuthenticatedUser(), name, purpose, start, end);
            insertEntity(p);
            return p;
        });
    }

    @Override
    public void insertEntity(Entity entity) {
        TransactionUtilities.transactionWrapped(this, () -> getRepository().persist(entity));
    }

    @Override
    public User getAuthenticatedUser() {
        return null;
    }

    @Override
    public void beginTransaction() {
        transactionManager.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        transactionManager.commitTransaction();
    }

    @Override
    public void rollbackTransaction() {
        transactionManager.rollbackTransaction();
    }

    @Override
    public boolean isActiveTransaction() {
        return transactionManager.isActiveTransaction();
    }

}

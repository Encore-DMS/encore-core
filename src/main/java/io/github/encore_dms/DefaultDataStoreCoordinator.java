package io.github.encore_dms;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.github.encore_dms.data.DataStore;

public class DefaultDataStoreCoordinator implements DataStoreCoordinator {

    private final String authenticatedUser;

    private DataStore primaryDataStore;

    private final DataContext.Factory dataContextFactory;

    @Inject
    public DefaultDataStoreCoordinator(@Assisted String authenticatedUser, @Assisted DataStore primaryDataStore, DataContext.Factory dataContextFactory) {
        this.authenticatedUser = authenticatedUser;
        this.primaryDataStore = primaryDataStore;
        this.dataContextFactory = dataContextFactory;
    }

    @Override
    public DataContext getContext() {
        return getContext(getPrimaryDataStore());
    }

    private DataContext getContext(DataStore ds) {
        return dataContextFactory.create(ds, this);
    }

    @Override
    public DataStore getPrimaryDataStore() {
        return primaryDataStore;
    }

    @Override
    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    @Override
    public boolean isAuthenticated() {
        return getAuthenticatedUser() != null;
    }

}

package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;

public class DefaultDataStoreCoordinator implements DataStoreCoordinator {

    private DataStore primaryDataStore;

    private final DataContext.Factory dataContextFactory;

    public DefaultDataStoreCoordinator(DataStore primaryDataStore, DataContext.Factory dataContextFactory) {
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

}

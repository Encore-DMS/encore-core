package io.github.encore_dms;

import com.google.inject.Inject;
import io.github.encore_dms.data.DataStore;

public class DefaultDataStoreCoordinatorConnection implements DataStoreCoordinator.Connection {

    private final DataStore.Factory primaryDataStoreFactory;
    private final DataStoreCoordinator.Factory dataStoreCoordinatorFactory;

    @Inject
    public DefaultDataStoreCoordinatorConnection(DataStore.Factory primaryDataStoreFactory, DataStoreCoordinator.Factory dataStoreCoordinatorFactory) {
        this.primaryDataStoreFactory = primaryDataStoreFactory;
        this.dataStoreCoordinatorFactory = dataStoreCoordinatorFactory;
    }

    public DataStoreCoordinator connect(String host, String username, String password) {
        DataStore store = primaryDataStoreFactory.create(host, username, password);
        return dataStoreCoordinatorFactory.create(username, store);
    }

}

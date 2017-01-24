package io.github.encore_dms;

import com.google.inject.Inject;
import io.github.encore_dms.data.DataStore;

public class DataStoreCoordinatorConnection implements DataStoreCoordinator.Connection {

    private final DataStore.Factory primaryDataStoreFactory;
    private final DataStoreCoordinator.Factory dataStoreCoordinatorFactory;

    @Inject
    public DataStoreCoordinatorConnection(DataStore.Factory primaryDataStoreFactory, DataStoreCoordinator.Factory dataStoreCoordinatorFactory) {
        this.primaryDataStoreFactory = primaryDataStoreFactory;
        this.dataStoreCoordinatorFactory = dataStoreCoordinatorFactory;
    }

    public DataStoreCoordinator connect(String url, String username, String password) {
        DataStore store = primaryDataStoreFactory.create(url, username, password);
        return dataStoreCoordinatorFactory.create(store);
    }

}

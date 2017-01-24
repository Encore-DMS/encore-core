package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;

public interface DataStoreCoordinator {

    DataContext getContext();

    DataStore getPrimaryDataStore();

    interface Connection {
        DataStoreCoordinator connect(String url, String username, String password);
    }

    interface Factory {
        DataStoreCoordinator create(DataStore primaryDataStore);
    }

}

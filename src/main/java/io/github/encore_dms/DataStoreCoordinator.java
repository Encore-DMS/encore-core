package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;

public interface DataStoreCoordinator {

    DataContext getContext();

    DataStore getPrimaryDataStore();

    String getAuthenticatedUser();

    boolean isAuthenticated();

    interface Connection {
        DataStoreCoordinator connect(String host, String username, String password);
    }

    interface Factory {
        DataStoreCoordinator create(String authenticatedUser, DataStore primaryDataStore);
    }

}

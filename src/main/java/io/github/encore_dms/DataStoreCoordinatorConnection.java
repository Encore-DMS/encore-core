package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.data.InMemoryDataStore;

public class DataStoreCoordinatorConnection {

    public DataStoreCoordinator connect(String url, String username, String password) {
        DataStore store = new InMemoryDataStore();
        return null;// new DataStoreCoordinator(store);
    }

}

package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;

public interface DataStoreCoordinator {
    DataContext getContext();

    DataStore getPrimaryDataStore();
}

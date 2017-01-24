package io.github.encore_dms;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Encore {

    private static final Injector injector = Guice.createInjector(new EncoreModule());

    public static DataStoreCoordinator connect(String host, String username, String password) {
        DataStoreCoordinator.Connection connection = injector.getInstance(DataStoreCoordinator.Connection.class);
        return connection.connect(host, username, password);
    }

}

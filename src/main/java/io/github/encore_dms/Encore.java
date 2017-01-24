package io.github.encore_dms;

public class Encore {

    public static DataStoreCoordinator connect(String url, String username, String password) {
        DataStoreCoordinatorConnection connection = new DataStoreCoordinatorConnection();
        return connection.connect(url, username, password);
    }

}

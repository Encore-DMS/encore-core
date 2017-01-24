package io.github.encore_dms.data;

import com.google.inject.assistedinject.Assisted;
import io.github.encore_dms.TransactionManager;

public interface DataStore {

    EntityDao getDao();

    TransactionManager getTransactionManager();

    interface Factory {
        DataStore create(@Assisted("host") String host, @Assisted("username") String username, @Assisted("password") String password);
    }

}

package io.github.encore_dms.data;

import io.github.encore_dms.TransactionManager;

public interface DataStore {

    EntityDao getDao();

    TransactionManager getTransactionManager();

}

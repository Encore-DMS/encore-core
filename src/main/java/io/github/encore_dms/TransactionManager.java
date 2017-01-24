package io.github.encore_dms;

public interface TransactionManager {

    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();

    boolean isActiveTransaction();

}

package io.github.encore_dms;

import javax.persistence.EntityTransaction;

public class DefaultTransactionManager implements TransactionManager {

    private final EntityTransaction transaction;
    private int transactionCount;

    public DefaultTransactionManager(EntityTransaction transaction) {
        this.transaction = transaction;
        this.transactionCount = 0;
    }

    @Override
    public synchronized void beginTransaction() {
        if (!isActiveTransaction()) {
            transaction.begin();
        }
        transactionCount++;
    }

    @Override
    public synchronized void commitTransaction() {
        if (transactionCount == 1) {
            transaction.commit();
        }
        transactionCount--;
    }

    @Override
    public synchronized void rollbackTransaction() {
        transaction.rollback();
        transactionCount = 0;
    }

    @Override
    public boolean isActiveTransaction() {
        return transaction.isActive();
    }

}

package io.github.encore_dms.util;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.TransactionManager;
import io.github.encore_dms.exceptions.EncoreException;

import java.util.concurrent.Callable;

public class TransactionUtilities {

    public static void transactionWrapped(TransactionManager transactionManager, Runnable block) {
        transactionManager.beginTransaction();
        try {
            block.run();
            transactionManager.commitTransaction();
        } catch (EncoreException x) {
            transactionManager.rollbackTransaction();
            throw x;
        } catch (Exception x) {
            transactionManager.rollbackTransaction();
            throw new EncoreException(x.getMessage(), x);
        }
    }

    public static <T> T transactionWrapped(TransactionManager transactionManager, Callable<T> block) {
        transactionManager.beginTransaction();
        try {
            T result = block.call();
            transactionManager.commitTransaction();
            return result;
        } catch (EncoreException x) {
            transactionManager.rollbackTransaction();
            throw x;
        } catch (Exception x) {
            transactionManager.rollbackTransaction();
            throw new EncoreException(x.getMessage(), x);
        }
    }

}

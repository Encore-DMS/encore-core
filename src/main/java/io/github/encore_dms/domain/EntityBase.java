package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.exceptions.EncoreException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.Callable;

@MappedSuperclass
abstract class EntityBase implements Entity {

    EntityBase(DataContext context) {
        this.dataContext = context;
    }

    protected EntityBase() {}

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Id
    private UUID uuid;

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public boolean canRead(User user) {
        return false;
    }

    @Override
    public boolean canWrite(User user) {
        return false;
    }

    @Transient
    private DataContext dataContext;

    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    void transactionWrapped(Runnable task) {
        DataContext context = getDataContext();

        context.beginTransaction();
        try {
            task.run();
            context.commitTransaction();
        } catch (Exception e) {
            context.rollbackTransaction();
            throw e;
        }
    }

    <T> T transactionWrapped(Callable<T> task) {
        DataContext context = getDataContext();

        context.beginTransaction();
        try {
            T result = task.call();
            context.commitTransaction();
            return result;
        } catch (EncoreException e) {
            context.rollbackTransaction();
            throw e;
        } catch (Exception e) {
            context.rollbackTransaction();
            throw new EncoreException("Transaction failed: " + e.getMessage(), e);
        }
    }

}

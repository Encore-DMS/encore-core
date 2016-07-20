package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
import java.util.concurrent.Callable;

@MappedSuperclass
abstract class AbstractEntity implements Entity {

    AbstractEntity(DataContext context, User owner) {
        this.dataContext = context;
        this.owner = owner;
    }

    protected AbstractEntity() {}

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Id
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    @ManyToOne
    private User owner;

    public User getOwner() {
        return owner;
    }

    private void setOwner(User owner) {
        transactionWrapped((Runnable) () -> this.owner = owner);
    }

    @Transient
    private DataContext dataContext;

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

    <T> T transactionWrapped(Callable<T> task) throws Exception {
        DataContext context = getDataContext();

        context.beginTransaction();
        try {
            T result = task.call();
            context.commitTransaction();
            return result;
        } catch (Exception e) {
            context.rollbackTransaction();
            throw e;
        }
    }

    public String toString() {
        return "UUID: " + getUuid() + "\n";
    }

}

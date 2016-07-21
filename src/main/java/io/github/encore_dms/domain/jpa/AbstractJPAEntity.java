package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
import java.util.concurrent.Callable;

@MappedSuperclass
abstract class AbstractJPAEntity implements io.github.encore_dms.domain.Entity {

    AbstractJPAEntity(DataContext context, User owner) {
        this.dataContext = context;
        this.owner = owner;
    }

    protected AbstractJPAEntity() {}

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Id
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    @ManyToOne(targetEntity = JPAUser.class)
    private User owner;

    public User getOwner() {
        return owner;
    }

    private void setOwner(User owner) {
        transactionWrapped((Runnable) () -> this.owner = owner);
    }

    @Transient
    private DataContext dataContext;

    DataContext getDataContext() {
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

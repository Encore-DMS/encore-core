package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.util.TransactionUtilities;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.Callable;

@MappedSuperclass
abstract class AbstractEntity implements Entity {

    AbstractEntity(DataContext context) {
        this.context = context;
    }

    protected AbstractEntity() {
    }

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
    private DataContext context;

    @Override
    public DataContext getDataContext() {
        return context;
    }

    void transactionWrapped(Runnable block) {
        TransactionUtilities.transactionWrapped(getDataContext(), block);
    }

    <T> T transactionWrapped(Callable<T> block) {
        return TransactionUtilities.transactionWrapped(getDataContext(), block);
    }

}

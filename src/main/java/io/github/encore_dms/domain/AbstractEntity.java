package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.util.TransactionUtilities;

import javax.persistence.*;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.Callable;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractEntity implements Entity {

    AbstractEntity(DataContext context) {
        this.context = context;
        this.uuid = UUID.randomUUID();
    }

    protected AbstractEntity() {}

    @Column(columnDefinition = "BINARY(16)")
    @Id
    private UUID uuid;

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof AbstractEntity))
            return false;

        AbstractEntity other = (AbstractEntity)obj;
        return getUuid().equals(other.getUuid());
    }

    @Transient
    private DataContext context;

    @Override
    public DataContext getDataContext() {
        return context;
    }

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

    void transactionWrapped(Runnable block) {
        TransactionUtilities.transactionWrapped(getDataContext(), block);
    }

    <T> T transactionWrapped(Callable<T> block) {
        return TransactionUtilities.transactionWrapped(getDataContext(), block);
    }

}

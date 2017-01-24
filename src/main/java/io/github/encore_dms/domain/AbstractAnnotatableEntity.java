package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.Owned;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class AbstractAnnotatableEntity extends AbstractEntity implements Owned {

    AbstractAnnotatableEntity(DataContext context, User owner) {
        super(context);
        this.owner = owner;
    }

    protected AbstractAnnotatableEntity() {
    }

    @ManyToOne(targetEntity = User.class)
    private User owner;

    @Override
    public User getOwner() {
        return owner;
    }

}

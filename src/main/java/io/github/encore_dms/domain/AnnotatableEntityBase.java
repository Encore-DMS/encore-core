package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.Owned;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class AnnotatableEntityBase extends EntityBase implements Owned {

    AnnotatableEntityBase(DataContext context, User owner) {
        super(context);
        this.owner = owner;
    }

    protected AnnotatableEntityBase() {}

    @ManyToOne(targetEntity = User.class)
    private User owner;

    @Override
    public User getOwner() {
        return owner;
    }

}

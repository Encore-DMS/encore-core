package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.domain.mixin.Owned;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class JPAAnnotatableEntityBase extends JPAEntityBase implements Owned {

    JPAAnnotatableEntityBase(DataContext context, User owner) {
        super(context);
        this.owner = owner;
    }

    protected JPAAnnotatableEntityBase() {}

    @ManyToOne(targetEntity = JPAUser.class)
    private User owner;

    @Override
    public User getOwner() {
        return owner;
    }

}

package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity(name = "User")
class JPAUser extends JPAEntityBase implements io.github.encore_dms.domain.User {

    JPAUser(DataContext context, String username, String password) {
        this.username = username;
    }

    protected JPAUser() {}

    @Basic
    private String username;

    @Override
    public String getUsername() {
        return username;
    }

    @Basic
    private char[] passwordHash;

}

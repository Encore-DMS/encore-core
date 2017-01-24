package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity {

    public User(DataContext context, String username, String password) {
        this.username = username;
    }

    protected User() {
    }

    @Basic
    private String username;

    public String getUsername() {
        return username;
    }

    @Basic
    private char[] passwordHash;

}

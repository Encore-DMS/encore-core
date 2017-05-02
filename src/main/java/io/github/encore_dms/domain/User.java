package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
public class User extends AbstractEntity {

    public User(DataContext context, String username, String password) {
        super(context);
        this.username = username;
    }

    protected User() {}

    @Basic
    private String username;

    public String getUsername() {
        return username;
    }

    @Basic
    private char[] passwordHash;

}

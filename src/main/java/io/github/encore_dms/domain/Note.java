package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import java.time.ZonedDateTime;

@javax.persistence.Entity
public class Note extends AbstractEntity {

    public Note(DataContext context, User owner, ZonedDateTime time, String text) {
        super(context, owner);
    }

    protected Note() {
    }

    @Basic
    private ZonedDateTime time;

    public ZonedDateTime getTime() {
        return time;
    }

    @Basic
    private String text;

    public String getText() {
        return text;
    }

}

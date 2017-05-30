package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import java.time.ZonedDateTime;
import java.util.Comparator;

@javax.persistence.Entity
public class Note extends AbstractEntity {

    public Note(DataContext context, ZonedDateTime time, String text) {
        super(context);
        this.time = time;
        this.text = text;
    }

    protected Note() {}

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

    static class TimeComparator implements Comparator<Note> {

        @Override
        public int compare(Note o1, Note o2) {
            if (o1 == o2)
                return 0;

            int result = o1.getTime().compareTo(o2.getTime());
            if (result != 0)
                return result;

            return o1.getUuid().compareTo(o2.getUuid());
        }

    }
}

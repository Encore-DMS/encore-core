package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.hibernate.annotations.SortComparator;

import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@javax.persistence.Entity
public class NoteSet extends AbstractEntity {

    public NoteSet(DataContext context) {
        super(context);
        this.notes = new TreeSet<>(new Note.TimeComparator());
    }

    protected NoteSet() {}

    @OneToMany
    @SortComparator(Note.TimeComparator.class)
    @OrderBy("time ASC")
    private SortedSet<Note> notes;

    public void add(Note note) {
        transactionWrapped(() -> {
            notes.add(note);
        });
    }

    public void remove(Note note) {
        transactionWrapped(() -> {
            notes.remove(note);
        });
    }

    public Stream<Note> getNotes() {
        return notes.stream();
    }

    public boolean isEmpty() {
        return notes.isEmpty();
    }

}
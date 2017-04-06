package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@javax.persistence.Entity
public class NoteSet extends AbstractEntity {

    public NoteSet(DataContext context) {
        super(context);
        this.notes = new HashSet<>();
    }

    protected NoteSet() {}

    @OneToMany
    @OrderBy("time ASC")
    private Set<Note> notes;

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

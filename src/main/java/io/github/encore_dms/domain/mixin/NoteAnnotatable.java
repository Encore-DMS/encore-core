package io.github.encore_dms.domain.mixin;

import com.google.common.collect.Multimap;
import io.github.encore_dms.domain.Note;
import io.github.encore_dms.domain.User;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface NoteAnnotatable {

    Note addNote(ZonedDateTime time, String text);

    void removeNote(Note note);

    Multimap<User, Note> getNotes();

    Stream<Note> getAllNotes();

    Stream<Note> getUserNotes(User user);

}

package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;
import io.github.encore_dms.values.NoteAnnotation;

import java.time.ZonedDateTime;
import java.util.Map;

public interface NoteAnnotatable {

    Map<User, NoteAnnotation> getNotes();

    NoteAnnotation addNote(ZonedDateTime time, String text);

    void removeNote(NoteAnnotation note);

}

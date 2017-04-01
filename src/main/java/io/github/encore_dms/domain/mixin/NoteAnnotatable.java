package io.github.encore_dms.domain.mixin;

import com.google.common.collect.Multimap;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.values.NoteAnnotation;

import java.time.ZonedDateTime;

public interface NoteAnnotatable {

    NoteAnnotation addNote(ZonedDateTime time, String text);

    void removeNote(NoteAnnotation note);

    Multimap<User, NoteAnnotation> getNotes();

}

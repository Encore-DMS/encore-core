package io.github.encore_dms.domain;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.KeywordAnnotatable;
import io.github.encore_dms.domain.mixin.NoteAnnotatable;
import io.github.encore_dms.domain.mixin.PropertyAnnotatable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractAnnotatableEntity extends AbstractEntity implements PropertyAnnotatable, KeywordAnnotatable, NoteAnnotatable {

    AbstractAnnotatableEntity(DataContext context, User owner) {
        super(context, owner);
        keywords = new HashMap<>();
        notes = new HashMap<>();
    }

    protected AbstractAnnotatableEntity() {
    }

    @Override
    public void addProperty(String key, Object value) {

    }

    @Override
    public void removeProperty(String key) {

    }

    @Override
    public Map<User, Map<String, Object>> getProperties() {
        return null;
    }

    @OneToMany(mappedBy = "entity")
    private Map<User, KeywordSet> keywords;

    @Override
    public void addKeyword(String keyword) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (!keywords.containsKey(user)) {
                KeywordSet k = new KeywordSet(c, user, this);
                c.insertEntity(k);
                keywords.put(user, k);
            }
            keywords.get(user).add(keyword);
        });
    }

    @Override
    public void removeKeyword(String keyword) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (keywords.containsKey(user)) {
                KeywordSet k = keywords.get(user);
                k.remove(keyword);
            }
        });
    }

    @Override
    public Multimap<User, String> getKeywords() {
        SetMultimap<User, String> result = HashMultimap.create();
        for (Map.Entry<User, KeywordSet> e : keywords.entrySet()) {
            result.putAll(e.getKey(), e.getValue().getKeywords()::iterator);
        }
        return Multimaps.unmodifiableMultimap(result);
    }

    @Override
    public Stream<String> getAllKeywords() {
        return keywords.values().stream().flatMap(KeywordSet::getKeywords);
    }

    @Override
    public Stream<String> getUserKeywords(User user) {
        return keywords.get(user).getKeywords();
    }

    @OneToMany(mappedBy = "entity")
    private Map<User, NoteSet> notes;

    @Override
    public Note addNote(ZonedDateTime time, String text) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (!notes.containsKey(user)) {
                NoteSet n = new NoteSet(c, user, this);
                c.insertEntity(n);
                notes.put(user, n);
            }
            Note note = new Note(c, user, time, text);
            c.insertEntity(note);
            notes.get(user).add(note);
            return note;
        });
    }

    @Override
    public void removeNote(Note note) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (notes.containsKey(user)) {
                NoteSet n = notes.get(user);
                n.remove(note);
            }
        });
    }

    @Override
    public Multimap<User, Note> getNotes() {
        SetMultimap<User, Note> result = HashMultimap.create();
        for (Map.Entry<User, NoteSet> e : notes.entrySet()) {
            result.putAll(e.getKey(), e.getValue().getNotes()::iterator);
        }
        return Multimaps.unmodifiableMultimap(result);
    }

    @Override
    public Stream<Note> getAllNotes() {
        return notes.values().stream().flatMap(NoteSet::getNotes);
    }

    @Override
    public Stream<Note> getUserNotes(User user) {
        return notes.get(user).getNotes();
    }

}

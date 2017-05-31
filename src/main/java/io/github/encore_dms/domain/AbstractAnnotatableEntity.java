package io.github.encore_dms.domain;

import com.google.common.collect.*;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.KeywordAnnotatable;
import io.github.encore_dms.domain.mixin.NoteAnnotatable;
import io.github.encore_dms.domain.mixin.Owned;
import io.github.encore_dms.domain.mixin.PropertyAnnotatable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractAnnotatableEntity extends AbstractEntity implements Owned, PropertyAnnotatable, KeywordAnnotatable, NoteAnnotatable {

    AbstractAnnotatableEntity(DataContext context, User owner) {
        super(context);
        this.owner = owner;
        this.properties = new HashMap<>();
        this.keywords = new HashMap<>();
        this.notes = new HashMap<>();
    }

    protected AbstractAnnotatableEntity() {}

    @ManyToOne
    private User owner;

    @Override
    public User getOwner() {
        return owner;
    }

    @OneToMany
    private Map<User, PropertyMap> properties;

    @Override
    public void addProperty(String key, Serializable value) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (!properties.containsKey(user)) {
                PropertyMap p = new PropertyMap(c);
                c.insertEntity(p);
                properties.put(user, p);
            }
            PropertyValue v = new PropertyValue(c, value);
            c.insertEntity(v);
            properties.get(user).put(key, v);
        });
    }

    @Override
    public void addProperty(String key, Object serializableValue) {
        if (!(serializableValue instanceof Serializable))
            throw new UnsupportedOperationException("Value must be instance of Serializable");

        addProperty(key, (Serializable) serializableValue);
    }

    @Override
    public void removeProperty(String key) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (properties.containsKey(user)) {
                PropertyMap p = properties.get(user);
                p.remove(key);
            }
        });
    }

    @Override
    public Map<User, Map<String, Object>> getProperties() {
        Map<User, Map<String, Object>> result = new HashMap<>();
        for (Map.Entry<User, PropertyMap> e : properties.entrySet()) {
            Map<String, Object> value = new HashMap<>();
            for (Map.Entry<String, PropertyValue> v : e.getValue().getProperties().entrySet()) {
                value.put(v.getKey(), v.getValue().getValue());
            }
            result.put(e.getKey(), Collections.unmodifiableMap(value));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<User, Object> getProperty(String key) {
        Map<User, Map<String, Object>> props = getProperties();

        Map<User, Object> result = new HashMap<>();
        for (Map.Entry<User, Map<String, Object>> e : props.entrySet()) {
            if (e.getValue().containsKey(key)) {
                result.put(e.getKey(), e.getValue().get(key));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Object getUserProperty(User user, String key) {
        Map<String, Object> props = getUserProperties(user);
        if (!props.containsKey(key)) {
            return null;
        }
        return props.get(key);
    }

    @Override
    public Map<String, Object> getUserProperties(User user) {
        if (!properties.containsKey(user)) {
            return ImmutableMap.of();
        }
        Map<String, Object> m = properties.get(user).getProperties().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, kv -> kv.getValue().getValue()));
        return Collections.unmodifiableMap(m);
    }

    @OneToMany
    private Map<User, KeywordSet> keywords;

    @Override
    public void addKeyword(String keyword) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (!keywords.containsKey(user)) {
                KeywordSet k = new KeywordSet(c);
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
        ListMultimap<User, String> result = LinkedListMultimap.create();
        for (Map.Entry<User, KeywordSet> e : keywords.entrySet()) {
            result.putAll(e.getKey(), e.getValue().getKeywords()::iterator);
        }
        return Multimaps.unmodifiableMultimap(result);
    }

    @Override
    public Stream<String> getAllKeywords() {
        return keywords.values().stream().flatMap(KeywordSet::getKeywords).distinct();
    }

    @Override
    public Stream<String> getUserKeywords(User user) {
        return keywords.get(user).getKeywords();
    }

    @OneToMany
    private Map<User, NoteSet> notes;

    @Override
    public Note addNote(ZonedDateTime time, String text) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            if (!notes.containsKey(user)) {
                NoteSet n = new NoteSet(c);
                c.insertEntity(n);
                notes.put(user, n);
            }
            Note note = new Note(c, time, text);
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
        ListMultimap<User, Note> result = LinkedListMultimap.create();
        for (Map.Entry<User, NoteSet> e : notes.entrySet()) {
            result.putAll(e.getKey(), e.getValue().getNotes()::iterator);
        }
        return Multimaps.unmodifiableMultimap(result);
    }

    @Override
    public Stream<Note> getAllNotes() {
        return notes.values().stream().flatMap(NoteSet::getNotes).sorted(new Note.TimeComparator());
    }

    @Override
    public Stream<Note> getUserNotes(User user) {
        return notes.get(user).getNotes();
    }

}

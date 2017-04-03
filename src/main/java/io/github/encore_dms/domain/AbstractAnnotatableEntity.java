package io.github.encore_dms.domain;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.KeywordAnnotatable;
import io.github.encore_dms.domain.mixin.NoteAnnotatable;
import io.github.encore_dms.domain.mixin.Owned;
import io.github.encore_dms.domain.mixin.PropertyAnnotatable;
import io.github.encore_dms.values.NoteAnnotation;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractAnnotatableEntity extends AbstractEntity implements Owned, PropertyAnnotatable, KeywordAnnotatable, NoteAnnotatable {

    AbstractAnnotatableEntity(DataContext context, User owner) {
        super(context);
        this.owner = owner;
        keywords = new HashMap<>();
    }

    protected AbstractAnnotatableEntity() {
    }

    @ManyToOne
    private User owner;

    @Override
    public User getOwner() {
        return owner;
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
            User owner = c.getAuthenticatedUser();
            if (!keywords.containsKey(owner)) {
                KeywordSet k = new KeywordSet(c, this);
                c.insertEntity(k);
                keywords.put(owner, k);
            }
            keywords.get(owner).add(keyword);
        });
    }

    @Override
    public void removeKeyword(String keyword) {
        transactionWrapped(() -> {
            DataContext c = getDataContext();
            User owner = c.getAuthenticatedUser();
            if (keywords.containsKey(owner)) {
                KeywordSet k = keywords.get(owner);
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

    @Override
    public NoteAnnotation addNote(ZonedDateTime time, String text) {
        return null;
    }

    @Override
    public void removeNote(NoteAnnotation note) {

    }

    @Override
    public Multimap<User, NoteAnnotation> getNotes() {
        return null;
    }

}

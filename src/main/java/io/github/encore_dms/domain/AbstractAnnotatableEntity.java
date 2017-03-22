package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.KeywordAnnotatable;
import io.github.encore_dms.domain.mixin.NoteAnnotatable;
import io.github.encore_dms.domain.mixin.Owned;
import io.github.encore_dms.domain.mixin.PropertyAnnotatable;
import io.github.encore_dms.values.NoteAnnotation;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.util.Map;

@MappedSuperclass
abstract class AbstractAnnotatableEntity extends AbstractEntity implements Owned, PropertyAnnotatable, KeywordAnnotatable, NoteAnnotatable {

    AbstractAnnotatableEntity(DataContext context, User owner) {
        super(context);
        this.owner = owner;
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
    public Map<User, Map<String, Object>> getProperties() {
        return null;
    }

    @Override
    public void addProperty(String key, Object value) {

    }

    @Override
    public void removeProperty(String key) {

    }

    @Override
    public Map<User, String> getKeywords() {
        return null;
    }

    @Override
    public void addKeyword(String keyword) {

    }

    @Override
    public void removeKeyword(String keyword) {

    }

    @Override
    public Map<User, NoteAnnotation> getNotes() {
        return null;
    }

    @Override
    public NoteAnnotation addNote(ZonedDateTime time, String text) {
        return null;
    }

    @Override
    public void removeNote(NoteAnnotation note) {

    }

}

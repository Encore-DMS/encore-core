package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@javax.persistence.Entity
public class KeywordSet extends AbstractEntity {

    public KeywordSet(DataContext context, AbstractEntity entity) {
        super(context);
        this.entity = entity;
        keywords = new HashSet<>();
    }

    protected KeywordSet() {
    }

    @ManyToOne
    private AbstractEntity entity;

    public AbstractEntity getEntity() {
        return entity;
    }

    @ElementCollection
    private Set<String> keywords;

    public void add(String keyword) {
        transactionWrapped(() -> {
            keywords.add(keyword);
        });
    }

    public void remove(String keyword) {
        transactionWrapped(() -> {
            keywords.remove(keyword);
        });
    }

    public Stream<String> getKeywords() {
        return keywords.stream();
    }

    public boolean isEmpty() {
        return keywords.isEmpty();
    }
}
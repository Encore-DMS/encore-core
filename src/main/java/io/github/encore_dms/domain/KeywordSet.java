package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.hibernate.annotations.SortNatural;

import javax.persistence.ElementCollection;
import javax.persistence.OrderBy;
import java.util.*;
import java.util.stream.Stream;

@javax.persistence.Entity
public class KeywordSet extends AbstractEntity {

    public KeywordSet(DataContext context) {
        super(context);
        this.keywords = new LinkedHashSet<>();
    }

    protected KeywordSet() {}

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

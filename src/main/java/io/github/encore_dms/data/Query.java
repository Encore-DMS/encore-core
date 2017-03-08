package io.github.encore_dms.data;

import java.util.UUID;
import java.util.stream.Stream;

public class Query<T> {

    private org.hibernate.query.Query<T> query;

    public Query(org.hibernate.query.Query<T> query) {
        this.query = query;
    }

    public Stream<T> stream() {
        return query.stream();
    }

    public Query<T> setParameter(String name, Object value) {
        query.setParameter(name, value);
        return this;
    }

}

package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@javax.persistence.Entity
public class PropertyMap extends AbstractEntity {

    public PropertyMap(DataContext context) {
        super(context);
        properties = new HashMap<>();
    }

    protected PropertyMap() {}

    @OneToMany
    private Map<String, PropertyValue> properties;

    public void put(String key, PropertyValue value) {
        transactionWrapped(() -> {
            properties.put(key, value);
        });
    }

    public void remove(String key) {
        transactionWrapped(() -> {
            properties.remove(key);
        });
    }

    public Map<String, PropertyValue> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

}

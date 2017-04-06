package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Lob;
import java.io.Serializable;

@javax.persistence.Entity
public class PropertyValue extends AbstractEntity {

    public PropertyValue(DataContext context, Serializable value) {
        super(context);
        this.value = value;
    }

    protected PropertyValue() {}

    @Lob
    private Serializable value;

    public Serializable getValue() {
        return value;
    }

}

package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Lob;

@javax.persistence.Entity
public class Resource extends AbstractAnnotatableEntity {

    public Resource(DataContext context, User owner, String name, byte[] data, String uti) {
        super(context, owner);
        this.name = name;
        this.data = data;
        this.uti = uti;
    }

    protected Resource() {}

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    @Lob
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    @Basic
    private String uti;

    public String getUti() {
        return uti;
    }

}

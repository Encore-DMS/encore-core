package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Source extends AbstractAnnotatableEntity {

    public Source(DataContext context, User owner, Source parent, String label) {
        super(context, owner);
        this.parent = parent;
        this.label = label;
    }

    protected Source() {
    }

    @ManyToOne
    private Source parent;

    public Source getParent() {
        return parent;
    }

    @Basic
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        transactionWrapped((Runnable) () -> this.label = label);
    }

}

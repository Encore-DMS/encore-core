package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity
public class Source extends AbstractAnnotatableEntity {

    public Source(DataContext context, User owner, Experiment experiment, Source parent, String label, String identifier) {
        super(context, owner);
        this.experiment = experiment;
        this.parent = parent;
        this.label = label;
        this.identifier = identifier;
        this.children = new LinkedList<>();
    }

    protected Source() {
    }

    @ManyToOne
    private Experiment experiment;

    public Experiment getExperiment() {
        return experiment;
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

    @Basic
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        transactionWrapped((Runnable) () -> this.identifier = identifier);
    }

    @OneToMany(mappedBy = "parent")
    private List<Source> children;

    public Source insertSource(String label, String identifier) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), getExperiment(), this, label, identifier);
            c.insertEntity(s);
            children.add(s);
            return s;
        });
    }

    public Stream<Source> getChildren() {
        return children.stream();
    }

}

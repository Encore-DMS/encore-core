package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.SourceContainer;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity
public class Source extends AbstractResourceAnnotatableEntity implements SourceContainer {

    public Source(DataContext context, User owner, Experiment experiment, Source parent, String label, ZonedDateTime creationTime, String identifier) {
        super(context, owner);
        this.experiment = experiment;
        this.parent = parent;
        this.label = label;
        this.creationTime = creationTime;
        this.identifier = identifier;
        this.children = new LinkedList<>();
    }

    protected Source() {}

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
    private ZonedDateTime creationTime;

    public ZonedDateTime getCreationTime() {
        return creationTime;
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
    @OrderBy("creationTime ASC")
    private List<Source> children;

    public Source insertSource(String label, ZonedDateTime creationTime, String identifier) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), getExperiment(), this, label, creationTime, identifier);
            c.insertEntity(s);
            children.add(s);
            children.sort(Comparator.comparing(Source::getCreationTime));
            return s;
        });
    }

    public Stream<Source> getSources() {
        return getChildren();
    }

    public Stream<Source> getChildren() {
        return children.stream();
    }

    public Stream<Source> getAllChildren() {
        return Stream.concat(getChildren(), getChildren().flatMap(Source::getAllChildren));
    }

}

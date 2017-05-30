package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.SourceContainer;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
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
        this.children = new TreeSet<>(new CreationTimeComparator());
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
    @SortComparator(CreationTimeComparator.class)
    @OrderBy("creationTime ASC")
    private SortedSet<Source> children;

    public Source insertSource(String label, ZonedDateTime creationTime, String identifier) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), getExperiment(), this, label, creationTime, identifier);
            c.insertEntity(s);
            children.add(s);
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
        return Stream.concat(getChildren(), getChildren().flatMap(Source::getAllChildren))
                .sorted(new CreationTimeComparator());
    }

    static class CreationTimeComparator implements Comparator<Source> {

        @Override
        public int compare(Source o1, Source o2) {
            if (o1 == o2)
                return 0;

            int result = o1.getCreationTime().compareTo(o2.getCreationTime());
            if (result != 0)
                return result;

            return o1.getUuid().compareTo(o2.getUuid());
        }

    }

}

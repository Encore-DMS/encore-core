package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.EpochGroupContainer;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@Entity
public class EpochGroup extends AbstractTimelineEntity implements EpochGroupContainer {

    public EpochGroup(DataContext context, User owner, Experiment experiment, EpochGroup parent, Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.experiment = experiment;
        this.parent = parent;
        this.source = source;
        this.label = label;
        this.children = new TreeSet<>(new TimelineComparator());
        this.epochBlocks = new TreeSet<>(new TimelineComparator());
    }

    protected EpochGroup() {}

    @ManyToOne
    private Experiment experiment;

    public Experiment getExperiment() {
        return experiment;
    }

    @ManyToOne
    private EpochGroup parent;

    public EpochGroup getParent() {
        return parent;
    }

    @ManyToOne
    private Source source;

    public Source getSource() {
        return source;
    }

    @Basic
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        transactionWrapped((Runnable) () -> this.label = label);
    }

    @OneToMany(mappedBy = "parent")
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<EpochGroup> children;

    public EpochGroup insertEpochGroup(Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochGroup g = new EpochGroup(c, c.getAuthenticatedUser(), getExperiment(), this, source, label, start, end);
            c.insertEntity(g);
            children.add(g);
            return g;
        });
    }

    public Stream<EpochGroup> getEpochGroups() {
        return getChildren();
    }

    public Stream<EpochGroup> getChildren() {
        return children.stream();
    }

    public Stream<EpochGroup> getAllChildren() {
        return Stream.concat(getChildren(), getChildren().flatMap(EpochGroup::getAllChildren))
                .sorted(new TimelineComparator());
    }

    @OneToMany(mappedBy = "epochGroup")
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<EpochBlock> epochBlocks;

    public EpochBlock insertEpochBlock(String protocolId, Map<String, Object> protocolParameters, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochBlock b = new EpochBlock(c, c.getAuthenticatedUser(), this, protocolId, protocolParameters, start, end);
            c.insertEntity(b);
            epochBlocks.add(b);
            return b;
        });
    }

    public Stream<EpochBlock> getEpochBlocks() {
        return epochBlocks.stream();
    }
}

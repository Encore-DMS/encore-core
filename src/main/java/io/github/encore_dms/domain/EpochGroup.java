package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Entity
public class EpochGroup extends AbstractTimelineEntity {

    public EpochGroup(DataContext context, User owner, Experiment experiment, EpochGroup parent, Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.experiment = experiment;
        this.parent = parent;
        this.source = source;
        this.label = label;
        this.children = new LinkedList<>();
        this.epochBlocks = new LinkedList<>();
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
    @OrderBy("startTime ASC")
    private List<EpochGroup> children;

    public EpochGroup insertEpochGroup(Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochGroup g = new EpochGroup(c, c.getAuthenticatedUser(), getExperiment(), this, source, label, start, end);
            c.insertEntity(g);
            children.add(g);
            children.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            return g;
        });
    }

    public Stream<EpochGroup> getChildren() {
        return children.stream();
    }

    @OneToMany(mappedBy = "epochGroup")
    @OrderBy("startTime ASC")
    private List<EpochBlock> epochBlocks;

    public EpochBlock insertEpochBlock(String protocolId, Map<String, Object> protocolParameters, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochBlock b = new EpochBlock(c, c.getAuthenticatedUser(), this, protocolId, protocolParameters, start, end);
            c.insertEntity(b);
            epochBlocks.add(b);
            epochBlocks.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            return b;
        });
    }

    public Stream<EpochBlock> getEpochBlocks() {
        return epochBlocks.stream();
    }
}

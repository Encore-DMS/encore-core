package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@Entity
public class EpochBlock extends AbstractTimelineEntity {

    public EpochBlock(DataContext context, User owner, EpochGroup epochGroup, String protocolId, Map<String, Object> protocolParameters, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.epochGroup = epochGroup;
        this.protocolId = protocolId;
        this.epochs = new TreeSet<>(new TimelineComparator());
    }

    protected EpochBlock() {}

    @ManyToOne
    private EpochGroup epochGroup;

    public EpochGroup getEpochGroup() {
        return epochGroup;
    }

    @Basic
    private String protocolId;

    public String getProtocolId() {
        return protocolId;
    }

    @OneToMany(mappedBy = "epochBlock")
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<Epoch> epochs;

    public Epoch insertEpoch(Map<String, Object> protocolParameters, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Epoch e = new Epoch(c, c.getAuthenticatedUser(), this, protocolParameters, start, end);
            c.insertEntity(e);
            epochs.add(e);
            return e;
        });
    }

    public Stream<Epoch> getEpochs() {
        return epochs.stream();
    }
}

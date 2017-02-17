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
public class EpochBlock extends AbstractTimelineEntity {

    public EpochBlock(DataContext context, User owner, EpochGroup epochGroup, String protocolId, Map<String, Object> parameters, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.epochGroup = epochGroup;
        this.protocolId = protocolId;
        this.epochs = new LinkedList<>();
    }

    protected EpochBlock() {
    }

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
    @OrderBy("startTime ASC")
    private List<Epoch> epochs;

    public Epoch insertEpoch(ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Epoch e = new Epoch(c, c.getAuthenticatedUser(), this, start, end);
            c.insertEntity(e);
            epochs.add(e);
            epochs.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            return e;
        });
    }

    public Stream<Epoch> getEpochs() {
        return epochs.stream();
    }
}

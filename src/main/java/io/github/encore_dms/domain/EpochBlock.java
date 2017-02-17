package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;
import java.util.Map;

public class EpochBlock extends AbstractTimelineEntity {

    public EpochBlock(DataContext context, User owner, EpochGroup epochGroup, String protocolId, Map<String, Object> parameters, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.epochGroup = epochGroup;
        this.protocolId = protocolId;
        this.parameters = parameters;
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

    private Map<String, Object> parameters;
}

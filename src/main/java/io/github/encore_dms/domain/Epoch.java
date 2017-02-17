package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Entity
public class Epoch extends AbstractTimelineEntity {

    public Epoch(DataContext context, User owner, EpochBlock epochBlock, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.epochBlock = epochBlock;
    }

    protected Epoch() {
    }

    @ManyToOne
    private EpochBlock epochBlock;

    public EpochBlock getEpochBlock() {
        return epochBlock;
    }

}

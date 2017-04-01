package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.TimelineElement;

import javax.persistence.Basic;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.ZonedDateTime;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractTimelineEntity extends AbstractAnnotatableEntity implements TimelineElement {

    AbstractTimelineEntity(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected AbstractTimelineEntity() {
    }

    @Basic
    private ZonedDateTime startTime;

    @Override
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    @Basic
    private ZonedDateTime endTime;

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }

}

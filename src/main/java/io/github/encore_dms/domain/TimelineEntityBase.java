package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.TimelineElement;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@MappedSuperclass
abstract class TimelineEntityBase extends AnnotatableEntityBase implements TimelineElement {

    TimelineEntityBase(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected TimelineEntityBase() {}

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

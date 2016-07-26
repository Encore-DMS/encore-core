package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.domain.mixin.TimelineElement;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@MappedSuperclass
abstract class JPATimelineEntityBase extends JPAAnnotatableEntityBase implements TimelineElement {

    JPATimelineEntityBase(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected JPATimelineEntityBase() {}

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

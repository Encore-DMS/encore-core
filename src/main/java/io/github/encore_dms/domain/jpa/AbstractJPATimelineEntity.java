package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.TimelineEntity;
import io.github.encore_dms.domain.User;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
abstract class AbstractJPATimelineEntity extends AbstractJPAEntity implements TimelineEntity {

    AbstractJPATimelineEntity(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected AbstractJPATimelineEntity() {}

    @Basic
    private ZonedDateTime startTime;

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    private void setStartTime(ZonedDateTime startTime) {
        transactionWrapped((Runnable) () -> this.startTime = startTime);
    }

    @Basic
    private ZonedDateTime endTime;

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    private void setEndTime(ZonedDateTime endTime) {
        transactionWrapped((Runnable) () -> this.endTime = endTime);
    }

    public String toString() {
        String s = super.toString();

        ZonedDateTime start = getStartTime();
        if (start != null) {
            s += "Start Date: " + start.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        }

        ZonedDateTime end = getEndTime();
        if (end != null) {
            s += "\nEnd Date: " + end.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        }

        return s + "\n";
    }

}

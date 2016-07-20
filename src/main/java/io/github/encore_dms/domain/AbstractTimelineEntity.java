package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
abstract class AbstractTimelineEntity extends AbstractEntity implements TimelineEntity {

    AbstractTimelineEntity(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected AbstractTimelineEntity() {}

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

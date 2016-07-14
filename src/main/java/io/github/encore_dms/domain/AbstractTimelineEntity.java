package io.github.encore_dms.domain;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
class AbstractTimelineEntity extends AbstractEntity {

    protected AbstractTimelineEntity(ZonedDateTime startTime, ZonedDateTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
    }

    protected AbstractTimelineEntity() {}

    @Basic
    private ZonedDateTime startTime;

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    @Basic
    private ZonedDateTime endTime;

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
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

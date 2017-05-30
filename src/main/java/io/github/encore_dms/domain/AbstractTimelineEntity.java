package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.TimelineElement;

import javax.persistence.Basic;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.ZonedDateTime;
import java.util.Comparator;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractTimelineEntity extends AbstractResourceAnnotatableEntity implements TimelineElement {

    AbstractTimelineEntity(DataContext context, User owner, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner);
        this.startTime = start;
        this.endTime = end;
    }

    protected AbstractTimelineEntity() {}

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

    static class TimelineComparator implements Comparator<AbstractTimelineEntity> {

        @Override
        public int compare(AbstractTimelineEntity o1, AbstractTimelineEntity o2) {
            if (o1 == o2)
                return 0;

            int result = o1.getStartTime().compareTo(o2.getStartTime());
            if (result != 0)
                return result;

            result = o1.getEndTime().compareTo(o2.getEndTime());
            if (result != 0)
                return result;

            return o1.getUuid().compareTo(o2.getUuid());
        }

    }
}

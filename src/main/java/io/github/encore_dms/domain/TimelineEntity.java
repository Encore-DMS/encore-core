package io.github.encore_dms.domain;

import java.time.ZonedDateTime;

public interface TimelineEntity extends Entity {

    ZonedDateTime getStartTime();

    ZonedDateTime getEndTime();

}

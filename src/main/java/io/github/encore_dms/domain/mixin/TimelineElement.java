package io.github.encore_dms.domain.mixin;

import java.time.ZonedDateTime;

public interface TimelineElement {

    ZonedDateTime getStartTime();

    ZonedDateTime getEndTime();

}

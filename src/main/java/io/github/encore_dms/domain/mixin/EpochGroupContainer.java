package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.EpochGroup;
import io.github.encore_dms.domain.Source;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface EpochGroupContainer {

    EpochGroup insertEpochGroup(Source source, String label, ZonedDateTime start, ZonedDateTime end);

    Stream<EpochGroup> getEpochGroups();

}

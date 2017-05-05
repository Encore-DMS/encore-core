package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.Source;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface SourceContainer {

    Source insertSource(String label, ZonedDateTime creationTime, String identifier);

    Stream<Source> getSources();

}

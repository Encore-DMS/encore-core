package io.github.encore_dms.domain;

import io.github.encore_dms.domain.mixin.Owned;
import io.github.encore_dms.domain.mixin.TimelineElement;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface Project extends Entity, TimelineElement, Owned {

    String getName();

    void setName(String name);

    String getPurpose();

    void setPurpose(String purpose);

    Stream<Experiment> getExperiments();

    Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end);

    void addExperiment(Experiment Experiment);

}

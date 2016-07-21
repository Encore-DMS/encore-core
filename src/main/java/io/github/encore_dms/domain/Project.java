package io.github.encore_dms.domain;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public interface Project extends Entity, TimelineEntity {

    String getName();

    void setName(String name);

    String getPurpose();

    void setPurpose(String purpose);

    Stream<Experiment> getExperiments();

    Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) throws Exception;

    void addExperiment(Experiment Experiment);

}

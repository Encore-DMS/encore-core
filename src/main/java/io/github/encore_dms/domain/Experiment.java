package io.github.encore_dms.domain;

import io.github.encore_dms.domain.mixin.Owned;
import io.github.encore_dms.domain.mixin.TimelineElement;

import java.util.stream.Stream;

public interface Experiment extends Entity, TimelineElement, Owned {

    String getPurpose();

    void setPurpose(String purpose);

    Stream<Project> getProjects();

    void addProject(Project project);

}

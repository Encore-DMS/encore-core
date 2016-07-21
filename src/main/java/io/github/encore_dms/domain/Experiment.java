package io.github.encore_dms.domain;

import java.util.stream.Stream;

public interface Experiment extends Entity, TimelineEntity {

    String getPurpose();

    void setPurpose(String purpose);

    Stream<Project> getProjects();

    void addProject(Project project);

}

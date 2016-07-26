package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity(name = "Experiment")
class JPAExperiment extends JPATimelineEntityBase implements io.github.encore_dms.domain.Experiment {

    JPAExperiment(DataContext context, User owner, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.purpose = purpose;
        this.projects = new LinkedList<>();
    }

    protected JPAExperiment() {}

    @Basic
    private String purpose;

    @Override
    public String getPurpose() {
        return purpose;
    }

    @Override
    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @ManyToMany(targetEntity = JPAProject.class, mappedBy = "experiments")
    @OrderBy("startTime ASC")
    private List<Project> projects;

    @Override
    public Stream<Project> getProjects() {
        return projects.stream();
    }

    @Override
    public void addProject(Project project) {
        transactionWrapped(() -> {
            if (!projects.contains(project)) {
                projects.add(project);
                projects.sort((p1, p2) -> p1.getStartTime().compareTo(p2.getStartTime()));
                project.addExperiment(this);
            }
        });
    }

}

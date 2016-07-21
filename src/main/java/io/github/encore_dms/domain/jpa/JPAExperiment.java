package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Entity(name = "Experiment")
class JPAExperiment extends AbstractJPATimelineEntity implements io.github.encore_dms.domain.Experiment {

    JPAExperiment(DataContext context, User owner, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.purpose = purpose;
        this.projects = new HashSet<>();
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
    private Set<Project> projects;

    public Stream<Project> getProjects() {
        return projects.stream();
    }

    @Override
    public void addProject(Project project) {
        transactionWrapped(() -> {
            if (!projects.contains(project)) {
                projects.add(project);
                project.addExperiment(this);
            }
        });
    }

}

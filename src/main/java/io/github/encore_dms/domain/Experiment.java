package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class Experiment extends AbstractTimelineEntity {

    public Experiment(DataContext context, User owner, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.purpose = purpose;
    }

    protected Experiment() {}

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @ManyToMany(mappedBy = "experiments")
    private Set<Project> projects;

    public Iterable<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        transactionWrapped((Runnable) () -> projects.add(project));
    }

}

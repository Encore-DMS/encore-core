package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity
public class Experiment extends AbstractTimelineEntity {

    public Experiment(DataContext context, User owner, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.purpose = purpose;
        this.projects = new LinkedList<>();
    }

    protected Experiment() {
    }

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @ManyToMany(targetEntity = Project.class, mappedBy = "experiments")
    @OrderBy("startTime ASC")
    private List<Project> projects;

    public Stream<Project> getProjects() {
        return projects.stream();
    }

    public void addProject(Project project) {
        transactionWrapped(() -> {
            if (!projects.contains(project)) {
                projects.add(project);
                projects.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
                project.addExperiment(this);
            }
        });
    }

}

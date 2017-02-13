package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity
public class Experiment extends AbstractTimelineEntity {

    public Experiment(DataContext context, User owner, Project project, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.projects = new LinkedList<>();
        if (project != null) {
            this.projects.add(project);
        }
        this.purpose = purpose;
        this.epochGroups = new LinkedList<>();
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

    @OneToMany(mappedBy = "experiment")
    @OrderBy("startTime ASC")
    private List<EpochGroup> epochGroups;

    public Stream<EpochGroup> getEpochGroups() {
        return epochGroups.stream();
    }

    public EpochGroup insertEpochGroup(String label, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochGroup g = new EpochGroup(c, c.getAuthenticatedUser(), this, label, start, end);
            epochGroups.add(g);
            epochGroups.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            c.insertEntity(g);
            return g;
        });
    }
}

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
        this.sources = new LinkedList<>();
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

    @ManyToMany(mappedBy = "experiments")
    @OrderBy("startTime ASC")
    private List<Project> projects;

    public void addProject(Project project) {
        transactionWrapped(() -> {
            if (!projects.contains(project)) {
                projects.add(project);
                projects.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
                project.addExperiment(this);
            }
        });
    }

    public Stream<Project> getProjects() {
        return projects.stream();
    }

    @OneToMany(mappedBy = "experiment")
    private List<Source> sources;

    public Source insertSource(String label) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), this, null, label);
            c.insertEntity(s);
            sources.add(s);
            return s;
        });
    }

    public Stream<Source> getSources() {
        return sources.stream();
    }

    @OneToMany(mappedBy = "experiment")
    @OrderBy("startTime ASC")
    private List<EpochGroup> epochGroups;

    public EpochGroup insertEpochGroup(Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochGroup g = new EpochGroup(c, c.getAuthenticatedUser(), this, null, source, label, start, end);
            c.insertEntity(g);
            epochGroups.add(g);
            epochGroups.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            return g;
        });
    }

    public Stream<EpochGroup> getEpochGroups() {
        return epochGroups.stream();
    }

}

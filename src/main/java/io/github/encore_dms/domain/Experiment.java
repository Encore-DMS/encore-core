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
@NamedQuery(name = "Experiment.getSourcesWithIdentifier", query = "SELECT s FROM Experiment e JOIN e.sources s WHERE e.uuid = :expUuid AND s.identifier = :srcId")
public class Experiment extends AbstractTimelineEntity {

    public Experiment(DataContext context, User owner, Project project, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.projects = new LinkedList<>();
        if (project != null) {
            this.projects.add(project);
        }
        this.purpose = purpose;
        this.sources = new LinkedList<>();
        this.devices = new LinkedList<>();
        this.epochGroups = new LinkedList<>();
    }

    protected Experiment() {
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

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @OneToMany(mappedBy = "experiment")
    private List<Source> sources;

    public Source insertSource(String label, String identifier) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), this, null, label, identifier);
            c.insertEntity(s);
            sources.add(s);
            return s;
        });
    }

    public Stream<Source> getSources() {
        return sources.stream();
    }

    public Stream<Source> getSourcesWithIdentifier(String identifier) {
        return getDataContext().getRepository().createNamedQuery("Experiment.getSourcesWithIdentifier", Source.class)
                .setParameter("expUuid", getUuid())
                .setParameter("srcId", identifier)
                .stream();
    }

    @OneToMany(mappedBy = "experiment")
    private List<Device> devices;

    public Device insertDevice(String name, String manufacturer) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Device d = new Device(c, c.getAuthenticatedUser(), this, name, manufacturer);
            c.insertEntity(d);
            devices.add(d);
            return d;
        });
    }

    public Stream<Device> getDevices() {
        return devices.stream();
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

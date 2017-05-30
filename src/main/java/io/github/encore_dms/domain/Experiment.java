package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.EpochGroupContainer;
import io.github.encore_dms.domain.mixin.SourceContainer;
import io.github.encore_dms.exceptions.EncoreException;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

@Entity
public class Experiment extends AbstractTimelineEntity implements SourceContainer, EpochGroupContainer {

    public Experiment(DataContext context, User owner, Project project, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.projects = new TreeSet<>(new TimelineComparator());
        if (project != null) {
            this.projects.add(project);
        }
        this.purpose = purpose;
        this.sources = new TreeSet<>(new Source.CreationTimeComparator());
        this.devices = new LinkedList<>();
        this.epochGroups = new TreeSet<>(new TimelineComparator());
    }

    protected Experiment() {}

    @ManyToMany(mappedBy = "experiments")
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<Project> projects;

    public void addProject(Project project) {
        transactionWrapped(() -> {
            boolean added = projects.add(project);
            if (added) {
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
    @SortComparator(Source.CreationTimeComparator.class)
    @OrderBy("creationTime ASC")
    private SortedSet<Source> sources;

    public Source insertSource(String label, ZonedDateTime creationTime, String identifier) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Source s = new Source(c, c.getAuthenticatedUser(), this, null, label, creationTime, identifier);
            c.insertEntity(s);
            sources.add(s);
            return s;
        });
    }

    public Stream<Source> getSources() {
        return sources.stream();
    }

    public Stream<Source> getAllSources() {
        return Stream.concat(getSources(), getSources().flatMap(Source::getAllChildren))
                .sorted(new Source.CreationTimeComparator());
    }

    public Stream<Source> getAllSourcesWithIdentifier(String identifier) {
        return Stream.concat(getSources(), getSources().flatMap(Source::getAllChildren))
                .filter(s -> s.getIdentifier().equals(identifier))
                .sorted(new Source.CreationTimeComparator());
    }

    @OneToMany(mappedBy = "experiment")
    private List<Device> devices;

    public Device insertDevice(String name, String manufacturer) {
        if (getDevice(name, manufacturer) != null)
            throw new EncoreException("Device " + name + " (" + manufacturer + ") already exists");

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

    public Device getDevice(String name, String manufacturer) {
        return getDevices()
                .filter(d -> Objects.equals(d.getName(), name) && Objects.equals(d.getManufacturer(), manufacturer))
                .findFirst().orElse(null);
    }

    @OneToMany(mappedBy = "experiment")
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<EpochGroup> epochGroups;

    public EpochGroup insertEpochGroup(Source source, String label, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            EpochGroup g = new EpochGroup(c, c.getAuthenticatedUser(), this, null, source, label, start, end);
            c.insertEntity(g);
            epochGroups.add(g);
            return g;
        });
    }

    public Stream<EpochGroup> getEpochGroups() {
        return epochGroups.stream();
    }

    public Stream<EpochGroup> getAllEpochGroups() {
        return Stream.concat(getEpochGroups(), getEpochGroups().flatMap(EpochGroup::getAllChildren))
                .sorted(new TimelineComparator());
    }

}

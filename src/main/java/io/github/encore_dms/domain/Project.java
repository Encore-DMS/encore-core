package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@Entity
@NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p ORDER BY p.startTime ASC, p.endTime ASC")
public class Project extends AbstractTimelineEntity {

    public Project(DataContext context, User owner, String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.name = name;
        this.purpose = purpose;
        this.experiments = new TreeSet<>(new TimelineComparator());
    }

    protected Project() {}

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        transactionWrapped((Runnable) () -> this.name = name);
    }

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @ManyToMany
    @SortComparator(TimelineComparator.class)
    @OrderBy("startTime ASC, endTime ASC")
    private SortedSet<Experiment> experiments;

    public Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Experiment e = new Experiment(c, c.getAuthenticatedUser(), this, purpose, start, end);
            c.insertEntity(e);
            experiments.add(e);
            return e;
        });
    }

    public void addExperiment(Experiment experiment) {
        transactionWrapped(() -> {
            boolean added = experiments.add(experiment);
            if (added) {
                experiment.addProject(this);
            }
        });
    }

    public Stream<Experiment> getExperiments() {
        return experiments.stream();
    }

}

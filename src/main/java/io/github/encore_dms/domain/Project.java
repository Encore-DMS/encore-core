package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p ORDER BY p.startTime")
public class Project extends AbstractTimelineEntity {

    public Project(DataContext context, User owner, String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.name = name;
        this.purpose = purpose;
        this.experiments = new LinkedList<>();
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
    @OrderBy("startTime ASC")
    private List<Experiment> experiments;

    public Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Experiment e = new Experiment(c, c.getAuthenticatedUser(), this, purpose, start, end);
            c.insertEntity(e);
            experiments.add(e);
            experiments.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            return e;
        });
    }

    public void addExperiment(Experiment experiment) {
        transactionWrapped(() -> {
            if (!experiments.contains(experiment)) {
                experiments.add(experiment);
                experiments.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
                experiment.addProject(this);
            }
        });
    }

    public Stream<Experiment> getExperiments() {
        return experiments.stream();
    }

}

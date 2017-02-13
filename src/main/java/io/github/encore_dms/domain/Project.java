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
public class Project extends AbstractTimelineEntity {

    public Project(DataContext context, User owner, String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.name = name;
        this.purpose = purpose;
        this.experiments = new LinkedList<>();
    }

    protected Project() {
    }

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

    public Stream<Experiment> getExperiments() {
        return experiments.stream();
    }

    public Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Experiment e = new Experiment(c, c.getAuthenticatedUser(), this, purpose, start, end);
            experiments.add(e);
            experiments.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));
            c.insertEntity(e);
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

}

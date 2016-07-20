package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class Project extends AbstractTimelineEntity {

    public Project(DataContext context, User owner, String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.name = name;
        this.purpose = purpose;
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

    public Iterable<Experiment> getExperiments() {
        return experiments;
    }

    public Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) throws Exception {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Experiment e = new Experiment(c, c.getAuthenticatedUser(), purpose, start, end);
            e.addProject(this);
            c.insertEntity(e);
            return e;
        });
    }

    public String toString() {
        return super.toString() + "Name: " + getName() + "\n";
    }

}

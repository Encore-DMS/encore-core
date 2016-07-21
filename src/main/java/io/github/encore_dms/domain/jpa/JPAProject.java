package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.Experiment;
import io.github.encore_dms.domain.User;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Entity(name = "Project")
class JPAProject extends AbstractJPATimelineEntity implements io.github.encore_dms.domain.Project {

    JPAProject(DataContext context, User owner, String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.name = name;
        this.purpose = purpose;
        this.experiments = new LinkedList<>();
    }

    protected JPAProject() {}

    @Basic
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        transactionWrapped((Runnable) () -> this.name = name);
    }

    @Basic
    private String purpose;

    @Override
    public String getPurpose() {
        return purpose;
    }

    @Override
    public void setPurpose(String purpose) {
        transactionWrapped((Runnable) () -> this.purpose = purpose);
    }

    @ManyToMany(targetEntity = JPAExperiment.class)
    @OrderBy("startTime ASC")
    private List<Experiment> experiments;

    @Override
    public Stream<Experiment> getExperiments() {
        return experiments.stream();
    }

    @Override
    public Experiment insertExperiment(String purpose, ZonedDateTime start, ZonedDateTime end) throws Exception {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Experiment e = new JPAExperiment(c, c.getAuthenticatedUser(), purpose, start, end);
            addExperiment(e);
            c.insertEntity(e);
            return e;
        });
    }

    @Override
    public void addExperiment(Experiment experiment) {
        transactionWrapped(() -> {
            if (!experiments.contains(experiment)) {
                experiments.add(experiment);
                experiments.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
                experiment.addProject(this);
            }
        });
    }

    public String toString() {
        return super.toString() + "Name: " + getName() + "\n";
    }

}

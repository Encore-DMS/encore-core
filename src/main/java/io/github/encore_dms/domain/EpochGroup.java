package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Entity
public class EpochGroup extends AbstractTimelineEntity {

    public EpochGroup(DataContext context, User owner, Experiment experiment, String label, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.experiment = experiment;
        this.label = label;
    }

    protected EpochGroup() {
    }

    @Basic
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        transactionWrapped((Runnable) () -> this.label = label);
    }

    @ManyToOne
    private Experiment experiment;

    public Experiment getExperiment() {
        return experiment;
    }

}

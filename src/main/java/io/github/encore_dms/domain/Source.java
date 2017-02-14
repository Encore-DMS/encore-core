package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Source extends AbstractAnnotatableEntity {

    public Source(DataContext context, User owner, Experiment experiment, String label) {
        super(context, owner);
        this.experiment = experiment;
        this.label = label;
    }

    protected Source() {
    }

    @ManyToOne
    private Experiment experiment;

    public Experiment getExperiment() {
        return experiment;
    }

    @Basic
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        transactionWrapped((Runnable) () -> this.label = label);
    }

}

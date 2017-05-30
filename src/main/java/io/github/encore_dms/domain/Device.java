package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Device extends AbstractResourceAnnotatableEntity {

    public Device(DataContext context, User owner, Experiment experiment, String name, String manufacturer) {
        super(context, owner);
        this.experiment = experiment;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    protected Device() {}

    @ManyToOne
    private Experiment experiment;

    public Experiment getExperiment() {
        return experiment;
    }

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    @Basic
    private String manufacturer;

    public String getManufacturer() {
        return manufacturer;
    }

}

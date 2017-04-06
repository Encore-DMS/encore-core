package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import java.util.Map;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AbstractSignal extends AbstractResourceAnnotatableEntity {

    AbstractSignal(DataContext context, User owner, Epoch epoch, Device device, Map<String, Object> deviceParameters, String units) {
        super(context, owner);
        this.epoch = epoch;
        this.device = device;
        this.units = units;
    }

    protected AbstractSignal() {}

    @ManyToOne
    private Epoch epoch;

    public Epoch getEpoch() {
        return epoch;
    }

    @ManyToOne
    private Device device;

    public Device getDevice() {
        return device;
    }

    @Basic
    private String units;

    public String getUnits() {
        return units;
    }
}

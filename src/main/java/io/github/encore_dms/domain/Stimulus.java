package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.util.Map;

@Entity
public class Stimulus extends AbstractSignal {

    public Stimulus(DataContext context, User owner, Epoch epoch, Device device, Map<String, Object> deviceParameters, String stimulusId, Map<String, Object> parameters, String units) {
        super(context, owner, epoch, device, deviceParameters, units);
        this.stimulusId = stimulusId;
    }

    protected Stimulus() {
    }

    @Basic
    private String stimulusId;

    public String getStimulusId() {
        return stimulusId;
    }

}

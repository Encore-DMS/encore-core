package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
public class Stimulus extends AbstractSignal {

    public Stimulus(DataContext context, User owner, Epoch epoch, Device device, Map<String, Object> deviceParameters, String stimulusId, Map<String, Object> parameters, String units, List<Double> data) {
        super(context, owner, epoch, device, deviceParameters, units);
        this.stimulusId = stimulusId;
        this.data = data;
    }

    protected Stimulus() {
    }

    @Basic
    private String stimulusId;

    public String getStimulusId() {
        return stimulusId;
    }

    @ElementCollection
    private List<Double> data;

    public Optional<List<Double>> getData() {
        return data != null ? Optional.of(data) : Optional.empty();
    }

}

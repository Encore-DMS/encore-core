package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.util.Map;

@Entity
public class Background extends AbstractSignal {

    public Background(DataContext context, User owner, Epoch epoch, Device device, Map<String, Object> deviceParameters, double value, String units, double sampleRate, String sampleRateUnits) {
        super(context, owner, epoch, device, deviceParameters, units);
        this.value = value;
        this.sampleRate = sampleRate;
        this.sampleRateUnits = sampleRateUnits;
    }

    protected Background() {}

    @Basic
    private double value;

    public double getValue() {
        return value;
    }

    @Basic
    private double sampleRate;

    public double getSampleRate() {
        return sampleRate;
    }

    @Basic
    private String sampleRateUnits;

    public String getSampleRateUnits() {
        return sampleRateUnits;
    }

}

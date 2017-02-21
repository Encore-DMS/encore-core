package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@Entity
public class Response extends AbstractSignal {

    public Response(DataContext context, User owner, Epoch epoch, Device device, Map<String, Object> deviceParameters, List<Double> data, String units, double sampleRate, String sampleRateUnits) {
        super(context, owner, epoch, device, deviceParameters, units);
        this.data = data;
        this.sampleRate = sampleRate;
        this.sampleRateUnits = sampleRateUnits;
    }

    protected Response() {
    }

    @ElementCollection
    private List<Double> data;

    public List<Double> getData() {
        return data;
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

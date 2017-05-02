package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Entity
public class Epoch extends AbstractTimelineEntity {

    public Epoch(DataContext context, User owner, EpochBlock epochBlock, Map<String, Object> protocolParameters, ZonedDateTime start, ZonedDateTime end) {
        super(context, owner, start, end);
        this.epochBlock = epochBlock;
        this.responses = new LinkedList<>();
        this.stimuli = new LinkedList<>();
        this.backgrounds = new LinkedList<>();
    }

    protected Epoch() {}

    @ManyToOne
    private EpochBlock epochBlock;

    public EpochBlock getEpochBlock() {
        return epochBlock;
    }

    @OneToMany(mappedBy = "epoch", targetEntity = AbstractSignal.class)
    private List<Response> responses;

    public Response insertResponse(Device device, Map<String, Object> deviceParameters, List<Double> data, String units, double sampleRate, String sampleRateUnits) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Response r = new Response(c, c.getAuthenticatedUser(), this, device, deviceParameters, data, units, sampleRate, sampleRateUnits);
            c.insertEntity(r);
            responses.add(r);
            return r;
        });
    }

    public Stream<Response> getResponses() {
        return responses.stream();
    }

    @OneToMany(mappedBy = "epoch", targetEntity = AbstractSignal.class)
    private List<Stimulus> stimuli;

    public Stimulus insertStimulus(Device device, Map<String, Object> deviceParameters, String stimulusId, Map<String, Object> parameters, String units, List<Double> data) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Stimulus s = new Stimulus(c, c.getAuthenticatedUser(), this, device, deviceParameters, stimulusId, parameters, units, data);
            c.insertEntity(s);
            stimuli.add(s);
            return s;
        });
    }

    public Stream<Stimulus> getStimuli() {
        return stimuli.stream();
    }

    @OneToMany(mappedBy = "epoch", targetEntity = AbstractSignal.class)
    private List<Background> backgrounds;

    public Background insertBackground(Device device, Map<String, Object> deviceParameters, double value, String units, double sampleRate, String sampleRateUnits) {
        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            Background b = new Background(c, c.getAuthenticatedUser(), this, device, deviceParameters, value, units, sampleRate, sampleRateUnits);
            c.insertEntity(b);
            backgrounds.add(b);
            return b;
        });
    }

    public Stream<Background> getBackgrounds() { return backgrounds.stream(); }

}

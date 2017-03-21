package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class EpochTest extends AbstractTest {

    private Epoch epoch;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, Object> protocolParameters = new HashMap<>();
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");
        epoch = new Epoch(context, null, null, protocolParameters, start, end);
    }

    @Test
    public void insertResponse() {
        Device device = new Device(context, null, null, "device name", "manufacturer name");
        Map<String, Object> deviceParameters = new HashMap<>();
        List<Double> data = new ArrayList<>(Arrays.asList(1d, 2d, 3d));
        String units = "mV";
        double sampleRate = 10;
        String sampleRateUnits = "Hz";

        Response r = epoch.insertResponse(device, deviceParameters, data, units, sampleRate, sampleRateUnits);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(device, r.getDevice());
        assertEquals(data, r.getData());
        assertEquals(units, r.getUnits());
        assertEquals(sampleRate, r.getSampleRate());
        assertEquals(sampleRateUnits, r.getSampleRateUnits());
        assertEquals(epoch, r.getEpoch());
    }

    @Test
    public void getResponses() {
        List<Response> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Double> data = new ArrayList<Double>(Collections.singletonList((double) i));
            Response r = epoch.insertResponse(null, null, data, "units", i, "runits");
            expected.add(r);
        }

        List<Response> actual = epoch.getResponses().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void insertStimulus() {
        Device device = new Device(context, null, null, "device name", "manufacturer name");
        Map<String, Object> deviceParameters = new HashMap<>();
        String stimulusId = "io.github.symphony_das.Pulse";
        Map<String, Object> parameters = new HashMap<>();
        String units = "pA";
        List<Double> data = new ArrayList<Double>(Arrays.asList(1d, 2d, 3d));

        Stimulus s = epoch.insertStimulus(device, deviceParameters, stimulusId, parameters, units, data);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(device, s.getDevice());
        assertEquals(stimulusId, s.getStimulusId());
        assertEquals(units, s.getUnits());
        assertEquals(epoch, s.getEpoch());
        assertEquals(data, s.getData().get());
    }

    @Test
    public void getStimuli() {
        List<Stimulus> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Stimulus s = epoch.insertStimulus(null, null, "stim" + i, null, "units", null);
            expected.add(s);
        }

        List<Stimulus> actual = epoch.getStimuli().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void insertBackground() {
        Device device = new Device(context, null, null, "device name", "manufacturer name");
        Map<String, Object> deviceParameters = new HashMap<>();
        double value = -60;
        String units = "pA";
        double sampleRate = 10;
        String sampleRateUnits = "Hz";

        Background b = epoch.insertBackground(device, deviceParameters, value, units, sampleRate, sampleRateUnits);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(device, b.getDevice());
        assertEquals(value, b.getValue());
        assertEquals(units, b.getUnits());
        assertEquals(sampleRate, b.getSampleRate());
        assertEquals(sampleRateUnits, b.getSampleRateUnits());
        assertEquals(epoch, b.getEpoch());
    }

    @Test
    public void getBackground() {
        List<Background> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Background b = epoch.insertBackground(null, null, i, "units", i+3, "runits");
            expected.add(b);
        }

        List<Background> actual = epoch.getBackgrounds().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}

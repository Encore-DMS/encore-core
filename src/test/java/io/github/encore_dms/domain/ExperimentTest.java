package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.EpochGroupContainer;
import io.github.encore_dms.domain.mixin.SourceContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class ExperimentTest extends AbstractTest {

    private Experiment experiment;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-06-30T17:12:13Z[GMT]");
        experiment = new Experiment(context, null, null, "experimental testing", start, end);
    }

    @Test
    public void setPurpose() {
        String purpose = "a new experiment purpose";
        assertNotEquals(experiment.getPurpose(), purpose);

        experiment.setPurpose(purpose);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(experiment.getPurpose(), purpose);
    }

    @Test
    public void addProject() {
        assertEquals(0, experiment.getProjects().count());
        Project p = new Project(context, null, "name", "purpose", ZonedDateTime.now(), ZonedDateTime.now());
        experiment.addProject(p);
        assertEquals(1, experiment.getProjects().count());
        assertEquals(p, experiment.getProjects().findFirst().orElse(null));
        assertEquals(experiment, p.getExperiments().findFirst().orElse(null));
    }

    @Test
    public void getProjects() {
        List<Project> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Project p = new Project(context, null, "name" + (i * 5 + k), "purpose", time, time.plusMonths(i));
                experiment.addProject(p);
                expected.add(p);
            }
        }
        expected.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));

        List<Project> actual = experiment.getProjects().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void insertSource() {
        String label = "source";
        ZonedDateTime creationTime = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");
        String identifier = "identifier";

        Source s = experiment.insertSource(label, creationTime, identifier);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(label, s.getLabel());
        assertEquals(creationTime, s.getCreationTime());
        assertEquals(identifier, s.getIdentifier());
        assertEquals(experiment, s.getExperiment());
    }

    @Test
    public void getSources() {
        List<Source> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Source s = experiment.insertSource("label" + (i * 5 + k), time, "id" + (i * 5 + k));
                expected.add(s);
            }
        }
        expected.sort(Comparator.comparing(Source::getCreationTime));

        List<Source> actual = experiment.getSources().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void getAllSources() {
        List<Source> expected = new ArrayList<>();
        SourceContainer parent = experiment;
        for (int i = 0; i < 3; i++) {
            Source s = parent.insertSource("label" + i, ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]"), "identifier" + i);
            expected.add(s);
            parent = s;
        }

        List<Source> actual = experiment.getAllSources().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void insertDevice() {
        String name = "device";
        String manufacturer = "acme";

        Device d = experiment.insertDevice(name, manufacturer);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(name, d.getName());
        assertEquals(manufacturer, d.getManufacturer());
        assertEquals(experiment, d.getExperiment());
    }

    @Test
    public void getDevices() {
        List<Device> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Device d = experiment.insertDevice("name" + i, "man" + i);
            expected.add(d);
        }

        List<Device> actual = experiment.getDevices().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void getDevice() {
        experiment.insertDevice("name2", "man1");
        Device expected = experiment.insertDevice("name2", "man2");
        experiment.insertDevice("name3", "man2");

        Optional<Device> actual = experiment.getDevice("name2", "man2");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void insertEpochGroup() {
        ZonedDateTime creationTime = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");
        Source source = new Source(context, null, null, null, "source label", creationTime, "id");
        String label = "epoch group";
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");

        EpochGroup g = experiment.insertEpochGroup(source, label, start, end);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(source, g.getSource());
        assertEquals(label, g.getLabel());
        assertEquals(start, g.getStartTime());
        assertEquals(end, g.getEndTime());
        assertEquals(experiment, g.getExperiment());
    }

    @Test
    public void getEpochGroups() {
        List<EpochGroup> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                EpochGroup g = experiment.insertEpochGroup(null, "label" + (i * 5 + k), time, time.plusMinutes(i));
                expected.add(g);
            }
        }
        expected.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));

        List<EpochGroup> actual = experiment.getEpochGroups().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void getAllEpochGroups() {
        List<EpochGroup> expected = new ArrayList<>();
        EpochGroupContainer parent = experiment;
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                EpochGroup g = parent.insertEpochGroup(null, "label" + (i * 5 + k), time, time.plusMinutes(i));
                expected.add(g);
                parent = g;
            }
        }
        expected.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));

        List<EpochGroup> actual = experiment.getAllEpochGroups().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}
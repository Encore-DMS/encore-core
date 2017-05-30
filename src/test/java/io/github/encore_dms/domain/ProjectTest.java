package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class ProjectTest extends AbstractTest {

    private Project project;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-25T11:12:13Z[GMT]");
        project = new Project(context, null, "test project", "testing purposes", start, end);
    }

    @Test
    public void setName() {
        String name = "a new project name";
        assertNotEquals(project.getName(), name);

        project.setName(name);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(project.getName(), name);
    }

    @Test
    public void setPurpose() {
        String purpose = "a new project purpose";
        assertNotEquals(project.getPurpose(), purpose);

        project.setPurpose(purpose);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(project.getPurpose(), purpose);
    }

    @Test
    public void insertExperiment() {
        String purpose = "experimental testing";
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T11:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T16:12:14Z[GMT]");

        Experiment e = project.insertExperiment(purpose, start, end);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(purpose, e.getPurpose());
        assertEquals(start, e.getStartTime());
        assertEquals(end, e.getEndTime());

        List<Project> projects = e.getProjects().collect(Collectors.toList());

        assertEquals(1, projects.size());
        assertEquals(project, projects.get(0));
    }

    @Test
    public void addExperiment() {
        assertEquals(0, project.getExperiments().count());
        Experiment e = new Experiment(context, null, project, "purpose", ZonedDateTime.now(), ZonedDateTime.now());
        project.addExperiment(e);
        assertEquals(1, project.getExperiments().count());
        assertEquals(e, project.getExperiments().findFirst().orElse(null));
        assertEquals(project, e.getProjects().findFirst().orElse(null));
    }

    @Test
    public void getExperiments() {
        List<Experiment> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Experiment e = project.insertExperiment("purpose" + (i * 5 + k), time, time.plusDays(i));
                expected.add(e);
            }
        }
        expected.sort(new AbstractTimelineEntity.TimelineComparator());

        List<Experiment> actual = project.getExperiments().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}
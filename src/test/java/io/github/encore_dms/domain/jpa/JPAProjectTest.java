package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.domain.Experiment;
import io.github.encore_dms.domain.Project;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JPAProjectTest extends JPATest {

    private Project project;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-25T11:12:13Z[GMT]");
        project = new JPAProject(context, null, "test project", "testing purposes", start, end);
    }

    @Test
    public void setName() throws Exception {
        String name = "a new project name";
        assertNotEquals(project.getName(), name);
        project.setName(name);
        assertEquals(project.getName(), name);
    }

    @Test
    public void setPurpose() throws Exception {
        String purpose = "a new project purpose";
        assertNotEquals(project.getPurpose(), purpose);
        project.setPurpose(purpose);
        assertEquals(project.getPurpose(), purpose);
    }

    @Test
    public void insertExperiment() throws Exception {
        String purpose = "experimental testing";
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T11:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T16:12:14Z[GMT]");

        Experiment e = project.insertExperiment(purpose, start, end);

        assertEquals(purpose, e.getPurpose());
        assertEquals(start, e.getStartTime());
        assertEquals(end, e.getEndTime());

        List<Project> projects = e.getProjects().collect(Collectors.toList());

        assertEquals(1, projects.size());
        assertEquals(project, projects.get(0));
    }

    @Test
    public void getExperiments() throws Exception {
        List<Experiment> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Experiment e = project.insertExperiment("purpose" + (i * 5 + k), time, time.plusDays(i));
                expected.add(e);
            }
        }
        expected.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));

        List<Experiment> actual = project.getExperiments().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void addExperiment() throws Exception {
        assertEquals(0, project.getExperiments().count());
        Experiment e = new JPAExperiment(context, null, "purpose", ZonedDateTime.now(), ZonedDateTime.now());
        project.addExperiment(e);
        assertEquals(1, project.getExperiments().count());
        assertEquals(e, project.getExperiments().findFirst().orElse(null));
        assertEquals(project, e.getProjects().findFirst().orElse(null));
    }

}
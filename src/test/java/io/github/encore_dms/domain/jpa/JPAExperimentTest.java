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

public class JPAExperimentTest extends AbstractJPATest {

    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-06-30T17:12:13Z[GMT]");
        experiment = new JPAExperiment(context, null, "experimental testing", start, end);
    }

    @Test
    public void setPurpose() throws Exception {
        String purpose = "a new experiment purpose";
        assertNotEquals(experiment.getPurpose(), purpose);
        experiment.setPurpose(purpose);
        assertEquals(experiment.getPurpose(), purpose);
    }

    @Test
    public void getProjects() throws Exception {
        List<Project> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Project p = new JPAProject(context, null, "name" + (i * 5 + k), "purpose", time, time.plusMonths(i));
                experiment.addProject(p);
                expected.add(p);
            }
        }
        expected.sort((p1, p2) -> p1.getStartTime().compareTo(p2.getStartTime()));

        List<Project> actual = experiment.getProjects().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void addProject() throws Exception {

    }

}
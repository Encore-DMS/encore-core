package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.domain.Experiment;
import io.github.encore_dms.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JPAExperimentTest extends JPATest {

    private Experiment experiment;

    @BeforeEach
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
        assertEquals(0, experiment.getProjects().count());
        Project p = new JPAProject(context, null, "name", "purpose", ZonedDateTime.now(), ZonedDateTime.now());
        experiment.addProject(p);
        assertEquals(1, experiment.getProjects().count());
        assertEquals(p, experiment.getProjects().findFirst().orElse(null));
        assertEquals(experiment, p.getExperiments().findFirst().orElse(null));
    }

}
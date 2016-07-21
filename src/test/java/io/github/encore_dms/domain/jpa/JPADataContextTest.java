package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.domain.Project;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class JPADataContextTest extends AbstractJPADomainTest {

    @Test
    public void close() throws Exception {
        assertTrue(context.isOpen());
        context.close();
        assertFalse(context.isOpen());
    }

    @Test
    public void insertProject() throws Exception {
        String name = "test project";
        String purpose = "testing purposes";
        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-25T11:12:13Z[GMT]");

        Project p = context.insertProject(name, purpose, start, end);

        assertEquals(name, p.getName());
        assertEquals(purpose, p.getPurpose());
        assertEquals(start, p.getStartTime());
        assertEquals(end, p.getEndTime());
    }

    @Test
    public void getProjects() throws Exception {
        List<Project> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Project p = context.insertProject("project" + (i * 5 + k), "purpose" + (i * 5 + k), time, time.plusMonths(i));
                expected.add(p);
            }
        }
        expected.sort((p1, p2) -> p1.getStartTime().compareTo(p2.getStartTime()));

        List<Project> actual = context.getProjects().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}
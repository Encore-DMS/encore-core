package io.github.encore_dms;

import io.github.encore_dms.domain.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DataContextTest {
    private EntityManagerFactory entityManagerFactory;
    private DataContext context;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        context = new DataContext(entityManagerFactory.createEntityManager());
    }

    @After
    public void tearDown() throws Exception {
        context.close();
        entityManagerFactory.close();
    }

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
        Set<Project> expected = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(i), ZoneId.of("America/Los_Angeles"));
            Project p = context.insertProject("project" + i, "purpose" + i, time, time.plusMonths(1));
            expected.add(p);
        }

        Set<Project> actual = new HashSet<>();
        context.getProjects().forEach(actual::add);

        assertEquals(expected, actual);
    }

}
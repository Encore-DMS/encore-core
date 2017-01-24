package io.github.encore_dms;

import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.domain.EntityRepository;
import io.github.encore_dms.domain.Project;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataContextTest extends AbstractTest {

    private DataContext context;

    @Mock
    private DataStore store;

    @Mock
    private TransactionManager transactionManager;

    @Mock
    private DataStoreCoordinator coordinator;

    @Mock
    private EntityRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(store.getTransactionManager()).thenReturn(transactionManager);

        EntityRepository.Factory factory = mock(EntityRepository.Factory.class);
        when(factory.create(any(), any())).thenReturn(repository);

        context = new DefaultDataContext(store, coordinator, factory);
    }

    @Test
    public void insertProject() throws Exception {
        String name = "test project";
        String purpose = "testing purposes";
        ZonedDateTime start = ZonedDateTime.parse("2016-06-30T12:30:40Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-25T11:12:13Z[GMT]");

        Project p = context.insertProject(name, purpose, start, end);

        InOrder inOrder = inOrder(transactionManager, repository);
        inOrder.verify(transactionManager, atLeastOnce()).beginTransaction();
        inOrder.verify(repository).persist(p);
        inOrder.verify(transactionManager, atLeastOnce()).commitTransaction();

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

        when(repository.getProjects()).thenReturn(expected.stream());

        List<Project> actual = context.getProjects().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}
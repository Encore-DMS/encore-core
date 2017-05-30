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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class EpochBlockTest extends AbstractTest {

    private EpochBlock block;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");
        block = new EpochBlock(context, null, null, "test.protocol", null, start, end);
    }

    @Test
    public void insertEpoch() {
        Map<String, Object> protocolParameters = new HashMap<>();
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");

        Epoch e = block.insertEpoch(protocolParameters, start, end);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(block, e.getEpochBlock());
        assertEquals(start, e.getStartTime());
        assertEquals(end, e.getEndTime());
    }

    @Test
    public void getEpochs() {
        List<Epoch> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                Map<String, Object> protocolParameters = new HashMap<>();
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                Epoch e = block.insertEpoch(protocolParameters, time, time.plusMinutes(i));
                expected.add(e);
            }
        }
        expected.sort(new AbstractTimelineEntity.TimelineComparator());

        List<Epoch> actual = block.getEpochs().collect(Collectors.toList());

        assertEquals(expected, actual);
    }
}

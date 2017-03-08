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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class EpochGroupTest extends AbstractTest {

    private EpochGroup group;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");
        group = new EpochGroup(context, null, null, null, null, "group label", start, end);
    }

    @Test
    public void setLabel() {
        String label = "a new label";
        assertNotEquals(group.getLabel(), label);

        group.setLabel(label);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(group.getLabel(), label);
    }

    @Test
    public void insertEpochGroup() {
        Source source = new Source(context, null, null, null, "source label", ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]"), "id");
        String label = "epoch group";
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");

        EpochGroup g = group.insertEpochGroup(source, label, start, end);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(source, g.getSource());
        assertEquals(label, g.getLabel());
        assertEquals(start, g.getStartTime());
        assertEquals(end, g.getEndTime());
        assertEquals(group, g.getParent());
    }

    @Test
    public void getChildren() {
        List<EpochGroup> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                EpochGroup g = group.insertEpochGroup(null, "label" + (i * 5 + k), time, time.plusMinutes(i));
                expected.add(g);
            }
        }
        expected.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));

        List<EpochGroup> actual = group.getChildren().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void insertEpochBlock() {
        String protocolId = "protocol.test";
        Map<String, Object> parameters = new HashMap<>();
        ZonedDateTime start = ZonedDateTime.parse("2016-07-01T12:01:10Z[GMT]");
        ZonedDateTime end = ZonedDateTime.parse("2016-07-01T13:12:14Z[GMT]");

        EpochBlock b = group.insertEpochBlock(protocolId, parameters, start, end);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(protocolId, b.getProtocolId());
        assertEquals(start, b.getStartTime());
        assertEquals(end, b.getEndTime());
    }

    @Test
    public void getEpochBlocks() {
        List<EpochBlock> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(k), ZoneId.of("America/Los_Angeles"));
                EpochBlock b = group.insertEpochBlock("protocol.test." + (i * 5 + k), null, time, time.plusMinutes(i));
                expected.add(b);
            }
        }
        expected.sort(Comparator.comparing(AbstractTimelineEntity::getStartTime));

        List<EpochBlock> actual = group.getEpochBlocks().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}

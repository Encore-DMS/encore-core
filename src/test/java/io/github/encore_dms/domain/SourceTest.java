package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public class SourceTest extends AbstractTest {

    private Source source;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        source = new Source(context, null, null, null, "source label", ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]"), "identifier");
    }

    @Test
    public void setLabel() {
        String label = "a new label";
        assertNotEquals(source.getLabel(), label);

        source.setLabel(label);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(source.getLabel(), label);
    }

    @Test
    public void insertSource() {
        String label = "source";
        ZonedDateTime creationTime = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");
        String identifier = "identifier";

        Source s = source.insertSource(label, creationTime, identifier);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(label, s.getLabel());
        assertEquals(creationTime, s.getCreationTime());
        assertEquals(identifier, s.getIdentifier());
        assertEquals(source, s.getParent());
        assertEquals(source.getExperiment(), s.getExperiment());
    }

    @Test
    public void getChildren() {
        List<Source> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Source s = source.insertSource("label" + i, ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]"), "identifier" + i);
            expected.add(s);
        }

        List<Source> actual = source.getChildren().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}

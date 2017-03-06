package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        source = new Source(context, null, null, null, "source label", "identifier");
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
        String identifier = "identifier";

        Source s = source.insertSource(label, identifier);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(label, s.getLabel());
        assertEquals(identifier, s.getIdentifier());
        assertEquals(source, s.getParent());
        assertEquals(source.getExperiment(), s.getExperiment());
    }

    @Test
    public void getChildren() {
        List<Source> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Source s = source.insertSource("label" + i, "identifier" + i);
            expected.add(s);
        }

        List<Source> actual = source.getChildren().collect(Collectors.toList());

        assertEquals(expected, actual);
    }

}

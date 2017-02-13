package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

        source = new Source(context, null, null, "source label");
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

}

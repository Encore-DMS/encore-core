package io.github.encore_dms.domain;

import com.google.common.collect.Multimap;
import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractAnnotatableEntityTest extends AbstractTest {

    private AbstractAnnotatableEntity entity;

    @Mock
    private DataContext context;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(context.getAuthenticatedUser()).thenReturn(user);

        entity = spy(new AbstractAnnotatableEntity(context, null) {});
    }

    @Test
    public void addKeyword() {
        Set<String> expected = new HashSet<>(Arrays.asList("one", "two", "three"));

        InOrder inOrder = inOrder(context);
        for (String word : expected) {
            entity.addKeyword(word);

            inOrder.verify(context, atLeastOnce()).beginTransaction();
            inOrder.verify(context, atLeastOnce()).commitTransaction();
        }

        Multimap<User, String> keywords = entity.getKeywords();

        assertEquals(1, keywords.keySet().size());
        assertEquals(expected, keywords.get(user));
    }



}

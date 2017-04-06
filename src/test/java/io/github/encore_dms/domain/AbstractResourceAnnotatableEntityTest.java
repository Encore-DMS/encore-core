package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

public class AbstractResourceAnnotatableEntityTest extends AbstractTest {

    private AbstractResourceAnnotatableEntity entity;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        entity = spy(new AbstractResourceAnnotatableEntity(context, null) {});
    }

    @Test
    public void addResource() {
        String name = "res";
        byte[] data = {1, 2, 3};
        String uti = "public.data";

        Resource r = entity.addResource(name, data, uti);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(name, r.getName());
        assertEquals(data, r.getData());
        assertEquals(uti, r.getUti());

        List<Resource> resources = entity.getResources().collect(Collectors.toList());

        assertEquals(1, resources.size());
        assertEquals(r, resources.get(0));
    }

    @Test
    public void removeResource() {
        List<Resource> expected = new ArrayList<>();

        Resource r1 = entity.addResource("r1", new byte[]{1, 2, 3}, "public.data1");
        Resource r2 = entity.addResource("r2", new byte[]{4, 5, 6}, "public.data2");
        Resource r3 = entity.addResource("r3", new byte[]{7, 8, 9}, "public.data3");
        expected.addAll(Arrays.asList(r1, r2, r3));

        entity.removeResource(r2.getName());
        expected.remove(r2);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        List<Resource> actual = entity.getResources().collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void getResource() {
        Resource r1 = entity.addResource("r1", new byte[]{1, 2, 3}, "public.data1");
        Resource r2 = entity.addResource("r2", new byte[]{4, 5, 6}, "public.data2");
        Resource r3 = entity.addResource("r3", new byte[]{7, 8, 9}, "public.data3");

        assertEquals(r2, entity.getResource(r2.getName()));
    }

    @Test
    public void getResourceNames() {
        List<String> expected = new ArrayList<>(Arrays.asList("one", "two", "three"));

        for (String n : expected) {
            entity.addResource(n, new byte[]{}, "data");
        }

        List<String> actual = entity.getResourceNames().collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

}

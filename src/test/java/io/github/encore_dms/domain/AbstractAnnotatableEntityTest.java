package io.github.encore_dms.domain;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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
        String keyword = "myKeyword";

        entity.addKeyword(keyword);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        Multimap<User, String> keywords = entity.getKeywords();

        assertEquals(1, keywords.keySet().size());
        assertEquals(1, keywords.get(user).size());
        assertTrue(keywords.get(user).contains(keyword));
    }

    @Test
    public void addKeywords() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        SetMultimap<User, String> expected = HashMultimap.create();
        expected.putAll(user1, Arrays.asList("one", "two", "three"));
        expected.putAll(user2, Arrays.asList("four", "five", "six"));

        for (Map.Entry<User, String> e : expected.entries()) {
            when(context.getAuthenticatedUser()).thenReturn(e.getKey());
            entity.addKeyword(e.getValue());
        }

        assertEquals(expected, entity.getKeywords());
    }

    @Test
    public void removeKeyword() {
        Set<String> expected = new HashSet<>(Arrays.asList("one", "two", "three"));

        for (String k : expected) {
            entity.addKeyword(k);
        }

        entity.removeKeyword("two");
        expected.remove("two");

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        Multimap<User, String> keywords = entity.getKeywords();

        assertEquals(1, keywords.keySet().size());
        assertEquals(expected, keywords.get(user));
    }

    @Test
    public void removeKeywords() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        SetMultimap<User, String> expected = HashMultimap.create();
        expected.putAll(user1, Arrays.asList("one", "two", "three"));
        expected.putAll(user2, Arrays.asList("four", "five", "six"));

        for (Map.Entry<User, String> e : expected.entries()) {
            when(context.getAuthenticatedUser()).thenReturn(e.getKey());
            entity.addKeyword(e.getValue());
        }

        when(context.getAuthenticatedUser()).thenReturn(user1);
        entity.removeKeyword("two");
        expected.remove(user1, "two");

        when(context.getAuthenticatedUser()).thenReturn(user2);
        entity.removeKeyword("five");
        expected.remove(user2, "five");

        assertEquals(expected, entity.getKeywords());
    }

    @Test
    public void getKeywordsUsesUnmodifiableView() {
        String keyword = "hello";
        entity.addKeyword(keyword);

        Multimap<User, String> keywords = entity.getKeywords();

        assertThrows(UnsupportedOperationException.class, () -> keywords.remove(user, keyword));
    }

    @Test
    public void getAllKeywords() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<String> expected = new ArrayList<>(Arrays.asList("one", "two", "three", "four"));

        when(context.getAuthenticatedUser()).thenReturn(user1);
        entity.addKeyword(expected.get(0));
        entity.addKeyword(expected.get(1));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        entity.addKeyword(expected.get(2));
        entity.addKeyword(expected.get(3));

        List<String> actual = entity.getAllKeywords().collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void getUserKeywords() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<String> expected = new ArrayList<>(Arrays.asList("one", "two"));

        when(context.getAuthenticatedUser()).thenReturn(user1);
        entity.addKeyword(expected.get(0));
        entity.addKeyword(expected.get(1));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        entity.addKeyword("three");
        entity.addKeyword("four");

        List<String> actual = entity.getUserKeywords(user1).collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

}

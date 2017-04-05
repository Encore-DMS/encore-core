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

import java.time.ZonedDateTime;
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

    @Test
    public void addNote() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");
        String text = "some note text";

        Note n = entity.addNote(time, text);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        assertEquals(time, n.getTime());
        assertEquals(text, n.getText());

        Multimap<User, Note> notes = entity.getNotes();

        assertEquals(1, notes.keySet().size());
        assertEquals(1, notes.get(user).size());
        assertTrue(notes.get(user).contains(n));
    }

    @Test
    public void addNotes() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        SetMultimap<User, Note> expected = HashMultimap.create();

        when(context.getAuthenticatedUser()).thenReturn(user1);
        expected.put(user1, entity.addNote(time.plusMinutes(1), "one"));
        expected.put(user1, entity.addNote(time.plusMinutes(2), "two"));
        expected.put(user1, entity.addNote(time.plusMinutes(3), "three"));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        expected.put(user2, entity.addNote(time.plusMinutes(4), "four"));
        expected.put(user2, entity.addNote(time.plusMinutes(5), "five"));
        expected.put(user2, entity.addNote(time.plusMinutes(6), "six"));

        assertEquals(expected, entity.getNotes());
    }

    @Test
    public void removeNote() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");

        Set<Note> expected = new HashSet<>();

        Note n1 = entity.addNote(time.plusMinutes(1), "one");
        Note n2 = entity.addNote(time.plusMinutes(2), "two");
        Note n3 = entity.addNote(time.plusMinutes(3), "three");
        expected.addAll(Arrays.asList(n1, n2, n3));

        entity.removeNote(n2);
        expected.remove(n2);

        InOrder inOrder = inOrder(context);
        inOrder.verify(context, atLeastOnce()).beginTransaction();
        inOrder.verify(context, atLeastOnce()).commitTransaction();

        Multimap<User, Note> notes = entity.getNotes();

        assertEquals(1, notes.keySet().size());
        assertEquals(expected, notes.get(user));
    }

    @Test
    public void removeNotes() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        SetMultimap<User, Note> expected = HashMultimap.create();

        when(context.getAuthenticatedUser()).thenReturn(user1);
        Note n1 = entity.addNote(time.plusMinutes(1), "one");
        Note n2 = entity.addNote(time.plusMinutes(2), "two");
        Note n3 = entity.addNote(time.plusMinutes(3), "three");
        expected.putAll(user1, Arrays.asList(n1, n2, n3));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        Note n4 = entity.addNote(time.plusMinutes(4), "four");
        Note n5 = entity.addNote(time.plusMinutes(5), "five");
        Note n6 = entity.addNote(time.plusMinutes(6), "six");
        expected.putAll(user2, Arrays.asList(n4, n5, n6));

        when(context.getAuthenticatedUser()).thenReturn(user1);
        entity.removeNote(n2);
        expected.remove(user1, n2);

        when(context.getAuthenticatedUser()).thenReturn(user2);
        entity.removeNote(n5);
        expected.remove(user2, n5);

        assertEquals(expected, entity.getNotes());
    }

    @Test
    public void getAllNotes() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<Note> expected = new ArrayList<>();

        when(context.getAuthenticatedUser()).thenReturn(user1);
        expected.add(entity.addNote(time.plusMinutes(1), "one"));
        expected.add(entity.addNote(time.plusMinutes(2), "two"));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        expected.add(entity.addNote(time.plusMinutes(3), "three"));
        expected.add(entity.addNote(time.plusMinutes(4), "four"));

        List<Note> actual = entity.getAllNotes().collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void getUserNotes() {
        ZonedDateTime time = ZonedDateTime.parse("2016-07-01T12:00:00Z[GMT]");

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<Note> expected = new ArrayList<>();

        when(context.getAuthenticatedUser()).thenReturn(user1);
        expected.add(entity.addNote(time.plusMinutes(1), "one"));
        expected.add(entity.addNote(time.plusMinutes(2), "two"));

        when(context.getAuthenticatedUser()).thenReturn(user2);
        entity.addNote(time.plusMinutes(3), "three");
        entity.addNote(time.plusMinutes(4), "four");

        List<Note> actual = entity.getUserNotes(user1).collect(Collectors.toList());

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

}

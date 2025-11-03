package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HistorySnapshot}.
 */
public class HistorySnapshotTest {

    @Test
    public void navigation_basicPrevNext() {
        HistorySnapshot s = new HistorySnapshot(
                Arrays.asList("add n:A", "list", "find alex"), "a");

        // Start at buffer; UP should give last command
        assertEquals("find alex", s.previous());
        // Another UP
        assertEquals("list", s.previous());
        // Another UP
        assertEquals("add n:A", s.previous());
        // No more previous; stays at first
        assertEquals("add n:A", s.previous());

        // DOWN back through history
        assertEquals("list", s.next());
        assertEquals("find alex", s.next());
        // At end → returns buffer
        assertEquals("a", s.next());
        // Stay at buffer
        assertEquals("a", s.next());
    }

    @Test
    public void buffer_updateOnlyWhenAtBufferPosition() {
        HistorySnapshot s = new HistorySnapshot(
                Arrays.asList("list"), "he");

        // At buffer initially; update buffer should take effect
        s.setCurrentBuffer("help");
        assertEquals("help", s.next()); // still buffer
        // Go up into history
        s.previous(); // "list"
        // Try to change buffer while in history; should have no effect yet
        s.setCurrentBuffer("help2");
        // Down once → buffer view (should reflect latest buffer value only at buffer)
        assertEquals("help2", s.next());
    }

    @Test
    public void emptyHistory_behaviour() {
        HistorySnapshot s = new HistorySnapshot(Arrays.asList(), "x");
        // No previous/next in empty history
        assertFalse(s.hasPrevious());
        // next() returns buffer; previous() also returns buffer
        assertEquals("x", s.next());
        assertEquals("x", s.previous());
    }
}

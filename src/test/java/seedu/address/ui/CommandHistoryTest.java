package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CommandHistory}.
 */
public class CommandHistoryTest {

    @Test
    public void add_ignoresEmptyAndConsecutiveDuplicates() {
        CommandHistory h = new CommandHistory();

        h.add(""); // ignore empty
        h.add("   "); // ignore whitespace-only
        h.add("list");
        h.add("list"); // ignore immediate duplicate
        h.add("edit 1 n/A"); // add new one
        h.add("edit 1 n/A"); // ignore immediate duplicate

        HistorySnapshot s = h.snapshot("");
        // History order: ["list", "edit 1 n/A"]; cursor starts at buffer (after last)
        assertTrue(s.hasPrevious());
        assertEquals("edit 1 n/A", s.previous());
        assertTrue(s.hasPrevious());
        assertEquals("list", s.previous());
    }

    @Test
    public void snapshot_preservesIndependentCursorAndBuffer() {
        CommandHistory h = new CommandHistory();
        h.add("find alex");
        h.add("list");

        // First snapshot with buffer "ad"
        HistorySnapshot s1 = h.snapshot("ad");
        // Move up twice
        s1.previous(); // "list"
        s1.previous(); // "find alex"

        // Second snapshot created later with different buffer
        HistorySnapshot s2 = h.snapshot("dele");
        // s2 should start at buffer, independent from s1's cursor
        assertEquals("dele", s2.next()); // stay at buffer
        assertEquals("dele", s2.next()); // still buffer
    }
}

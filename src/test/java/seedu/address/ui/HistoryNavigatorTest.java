package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HistoryNavigatorTest {

    @Test
    void upDown_basicCycle() {
        var h = new HistoryNavigator();
        h.add("a");
        h.add("b");
        h.add("c");

        assertEquals("c", h.up());
        assertEquals("b", h.up());
        assertEquals("a", h.up());
        assertEquals("b", h.down());
        assertEquals("c", h.down());
    }

    @Test
    void down_returnsBufferAfterEnd() {
        var h = new HistoryNavigator();
        h.add("one");
        h.setBuffer("typing...");
        h.up(); // now on "one"
        assertEquals("typing...", h.down()); // exit history → buffer
    }
}

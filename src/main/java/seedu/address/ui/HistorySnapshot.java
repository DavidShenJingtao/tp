package seedu.address.ui;

import java.util.List;

/**
 * Represents an immutable snapshot of a {@link CommandHistory}.
 * <p>
 * The snapshot provides a movable cursor that allows navigation
 * through previously entered commands while preserving the user’s
 * current unfinished input (the "buffer").
 */
public class HistorySnapshot {

    private final List<String> entries;
    private String currentBuffer;
    private int cursor; // 0..size inclusive; size = current buffer

    HistorySnapshot(List<String> entries, String currentBuffer) {
        this.entries = entries;
        this.currentBuffer = currentBuffer == null ? "" : currentBuffer;
        this.cursor = entries.size(); // Start at buffer position
    }

    /**
     * Returns true if there is a previous command before the current position.
     */
    public boolean hasPrevious() {
        return cursor > 0;
    }

    /**
     * Returns true if there is a next command after the current position.
     */
    public boolean hasNext() {
        return cursor < entries.size();
    }

    /**
     * Moves the cursor one step backward (to an older command) and returns it.
     * If there is no previous command, the current text is returned unchanged.
     *
     * @return The previous command text or the current buffer if none.
     */
    public String previous() {
        if (!hasPrevious()) {
            return getCurrentView();
        }
        cursor--;
        return entries.get(cursor);
    }

    /**
     * Moves the cursor one step forward (to a newer command) and returns it.
     * If at the end of the history, returns the current buffer text.
     *
     * @return The next command text or the current buffer if none.
     */
    public String next() {
        if (!hasNext()) {
            return getCurrentView();
        }
        cursor++;
        return getCurrentView();
    }

    /**
     * Returns the text currently displayed based on the cursor position.
     */
    private String getCurrentView() {
        return cursor == entries.size() ? currentBuffer : entries.get(cursor);
    }

    /**
     * Updates the buffer text. The buffer is what will be shown when the cursor
     * is at the end of history (i.e., the editable input).
     *
     * @param text The text currently being typed by the user.
     */
    public void setCurrentBuffer(String text) {
        // Update buffer regardless of cursor position, so returning to buffer shows latest edits.
        currentBuffer = text == null ? "" : text;
    }
}

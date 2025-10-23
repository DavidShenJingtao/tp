package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logic for navigating through previously entered commands using
 * the ↑ and ↓ keys, while maintaining a live buffer for the user's current input.
 * <p>
 * This class is independent of JavaFX and can be tested directly.
 * It simulates the behavior of a command history in a terminal or chat application:
 * <ul>
 *     <li>Pressing ↑ moves to older commands.</li>
 *     <li>Pressing ↓ moves to newer commands or returns to the unfinished buffer.</li>
 * </ul>
 * </p>
 */
public class HistoryNavigator {

    /** Stores the list of previously executed commands in order of entry. */
    private final List<String> history = new ArrayList<>();

    /** Current index in the history list. -1 indicates that the user is at the live buffer. */
    private int idx = -1;

    /** Stores the current unfinished input that is not yet part of the history. */
    private String buffer = "";

    /**
     * Adds a command to the command history and resets the cursor to the buffer position.
     *
     * @param cmd The command string to add to history.
     *            If {@code null} or empty, the command is ignored.
     */
    public void add(String cmd) {
        if (cmd != null && !cmd.isEmpty()) {
            history.add(cmd);
        }
        idx = -1;
        buffer = "";
    }

    /**
     * Updates the live buffer with the user's current typing.
     * <p>
     * The buffer only updates when the cursor is at the buffer position (i.e., not browsing history).
     * </p>
     *
     * @param text The current text being typed by the user.
     *             If {@code null}, it is treated as an empty string.
     */
    public void setBuffer(String text) {
        if (idx == -1) {
            buffer = (text == null) ? "" : text;
        }
    }

    /**
     * Moves one step up the history (to an older command).
     * <p>
     * If the history is empty, the buffer is returned unchanged.
     * If the user is currently at the buffer, navigation starts at the most recent command.
     * </p>
     *
     * @return The previous command from history, or the buffer if history is empty.
     */
    public String up() {
        if (history.isEmpty()) {
            return buffer;
        }
        if (idx == -1) {
            idx = history.size() - 1;
        } else if (idx > 0) {
            idx--;
        }
        return history.get(idx);
    }

    /**
     * Moves one step down the history (to a newer command) or returns to the live buffer.
     * <p>
     * If the history is empty, the buffer is returned unchanged.
     * If the cursor is at the newest command and moves down again,
     * the cursor resets to the buffer position.
     * </p>
     *
     * @return The next command from history, or the current buffer if at the end of history.
     */
    public String down() {
        if (history.isEmpty()) {
            return buffer;
        }
        if (idx == -1) {
            return buffer;
        }
        if (idx < history.size() - 1) {
            idx++;
            return history.get(idx);
        } else {
            idx = -1;
            return buffer;
        }
    }
}

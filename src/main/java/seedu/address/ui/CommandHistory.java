package seedu.address.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stores a list of previously entered commands.
 * <p>
 * CommandHistory records all non-empty command strings entered by the user,
 * allowing navigation through them using the UP and DOWN arrow keys.
 */
public class CommandHistory {

    private final List<String> entries = new ArrayList<>();

    /**
     * Adds a command text to the history if it is not empty and not a duplicate
     * of the most recently added command.
     *
     * @param commandText The command entered by the user.
     */
    public void add(String commandText) {
        String trimmed = commandText == null ? "" : commandText.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        if (!entries.isEmpty() && Objects.equals(entries.get(entries.size() - 1), trimmed)) {
            return; // Skip consecutive duplicates
        }
        entries.add(trimmed);
    }

    /**
     * Returns a new {@code HistorySnapshot} of this command history.
     * The snapshot contains an independent movable cursor and a copy of the
     * current buffer text.
     *
     * @param currentBuffer The text currently being typed by the user.
     * @return A snapshot for navigating through the command history.
     */
    public HistorySnapshot snapshot(String currentBuffer) {
        return new HistorySnapshot(Collections.unmodifiableList(entries), currentBuffer);
    }
}

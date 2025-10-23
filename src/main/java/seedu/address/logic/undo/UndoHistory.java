package seedu.address.logic.undo;

import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Deque;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Maintains the undo history for state-changing commands.
 */
public final class UndoHistory {

    private static final class UndoEntry {
        private final AddressBook state;
        private final String label;

        private UndoEntry(AddressBook state, String label) {
            this.state = state;
            this.label = label;
        }
    }

    private static final Deque<UndoEntry> history = new ArrayDeque<>();

    private UndoHistory() {
    }

    /**
     * Records a snapshot of the address book before a state-changing command is executed.
     */
    public static void recordState(ReadOnlyAddressBook state, String commandLabel) {
        requireNonNull(state);
        requireNonNull(commandLabel);
        history.push(new UndoEntry(new AddressBook(state), commandLabel));
    }

    /**
     * Returns {@code true} if there is a previous state to restore.
     */
    public static boolean canUndo() {
        return !history.isEmpty();
    }

    /**
     * Restores the most recently saved state into the provided {@code model}.
     */
    public static String restorePreviousState(Model model) {
        requireNonNull(model);
        if (!canUndo()) {
            return null;
        }
        UndoEntry entry = history.pop();
        model.setAddressBook(entry.state);
        return entry.label;
    }

    /**
     * Clears all recorded states. Intended for use in tests.
     */
    public static void clear() {
        history.clear();
    }
}

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

    private static final Deque<AddressBook> history = new ArrayDeque<>();

    private UndoHistory() {
    }

    /**
     * Records a snapshot of the address book before a state-changing command is executed.
     */
    public static void recordState(ReadOnlyAddressBook state) {
        requireNonNull(state);
        history.push(new AddressBook(state));
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
    public static void restorePreviousState(Model model) {
        requireNonNull(model);
        if (!canUndo()) {
            return;
        }
        AddressBook previousState = history.pop();
        model.setAddressBook(previousState);
    }

    /**
     * Clears all recorded states. Intended for use in tests.
     */
    public static void clear() {
        history.clear();
    }
}


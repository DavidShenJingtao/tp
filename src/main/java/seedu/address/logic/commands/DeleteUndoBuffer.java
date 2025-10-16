package seedu.address.logic.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import seedu.address.model.person.Person;

/**
 * Simple in-memory buffer to support undoing the most recent delete operation.
 * Stores the last batch of deleted persons in insertion order.
 */
public final class DeleteUndoBuffer {
    private static final Deque<List<Person>> history = new ArrayDeque<>();

    private DeleteUndoBuffer() {}

    public static void pushBatch(List<Person> persons) {
        history.push(new ArrayList<>(persons));
    }

    public static List<Person> popLatest() {
        return history.isEmpty() ? null : history.pop();
    }

    // For tests
    public static void clear() {
        history.clear();
    }
}


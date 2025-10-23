package seedu.address.model;

import seedu.address.model.person.Session;

/**
 * Unmodifiable view of {@code PersonAndSessionCounter}
 */
public interface ReadOnlyPersonAndSessionCounter {
    /**
     * Returns number of persons in address book,
     * if a single person was added to the address book.
     */
    short getPersonCountIfPersonAdded();

    /**
     * Returns number of unique sessions in address book,
     * if a {@code Session s} was added to the address book.
     */
    short getUniqueSessionCountIfSessionAdded(Session s);
}

package seedu.address.model;

import java.util.Set;

import seedu.address.model.person.Session;

/**
 * Unmodifiable view of {@code PersonAndSessionCounter}
 */
public interface ReadOnlyPersonAndSessionCounter {
    /**
     * Returns number of persons in TAConnect,
     * if a single person was added to the TAConnect.
     */
    short getPersonCountIfPersonAdded();

    /**
     * Returns number of unique sessions in TAConnect,
     * if a {@code Session s} was added to the TAConnect.
     */
    short getUniqueSessionCountIfSessionAdded(Session s);

    /**
     * List all sessions currently existing in TAConnect
     */

    Set<Session> listAllSessions();
}

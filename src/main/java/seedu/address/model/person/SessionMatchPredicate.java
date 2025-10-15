package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Session} matches any of the sessions given.
 */
public class SessionMatchPredicate implements Predicate<Person> {
    private final List<Session> sessions;

    public SessionMatchPredicate(List<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public boolean test(Person person) {
        return person.getSession().map(sessions::contains).orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SessionMatchPredicate)) {
            return false;
        }

        SessionMatchPredicate otherSessionMatchPredicate = (SessionMatchPredicate) other;
        return sessions.equals(otherSessionMatchPredicate.sessions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("sessions", sessions).toString();
    }
}

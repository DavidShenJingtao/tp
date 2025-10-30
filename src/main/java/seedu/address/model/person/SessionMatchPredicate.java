package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Session} matches the sessions given.
 */
public class SessionMatchPredicate implements Predicate<Person> {
    private final Session session;

    public SessionMatchPredicate(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean test(Person person) {
        return person.getSession().map(session::equals).orElse(false);
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
        return session.equals(otherSessionMatchPredicate.session);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("session", session).toString();
    }
}

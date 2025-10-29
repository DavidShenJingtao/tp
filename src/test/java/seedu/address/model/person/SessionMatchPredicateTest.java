package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class SessionMatchPredicateTest {

    @Test
    public void equals() {
        Session firstPredicateSession = new Session("S1");
        Session secondPredicateSession = new Session("S2");

        SessionMatchPredicate firstPredicate = new SessionMatchPredicate(firstPredicateSession);
        SessionMatchPredicate secondPredicate = new SessionMatchPredicate(secondPredicateSession);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        SessionMatchPredicate firstPredicateCopy = new SessionMatchPredicate(firstPredicateSession);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personContainsSession_returnsTrue() {
        // One session input match
        SessionMatchPredicate predicate = new SessionMatchPredicate(new Session("S1"));
        assertTrue(predicate.test(new PersonBuilder().withSession("S1").build()));
    }

    @Test
    public void test_personDoesNotExistInSession_returnsFalse() {
        // Non-matching session
        SessionMatchPredicate predicate = new SessionMatchPredicate(new Session("S1"));
        assertFalse(predicate.test(new PersonBuilder().withSession("S2").build()));
    }

    @Test
    public void toStringMethod() {
        Session session = new Session("S1");
        SessionMatchPredicate predicate = new SessionMatchPredicate(session);

        String expected = SessionMatchPredicate.class.getCanonicalName() + "{session=" + session + "}";
        assertEquals(expected, predicate.toString());
    }
}

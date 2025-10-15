package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class SessionMatchPredicateTest {

    @Test
    public void equals() {
        List<Session> firstPredicateSessionList = Collections.singletonList(new Session("S1"));
        List<Session> secondPredicateSessionList = Arrays.asList(new Session("S1"), new Session("S2"));

        SessionMatchPredicate firstPredicate = new SessionMatchPredicate(firstPredicateSessionList);
        SessionMatchPredicate secondPredicate = new SessionMatchPredicate(secondPredicateSessionList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        SessionMatchPredicate firstPredicateCopy = new SessionMatchPredicate(firstPredicateSessionList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsSessions_returnsTrue() {
        // One session input match
        SessionMatchPredicate predicate = new SessionMatchPredicate(Arrays.asList(new Session("S1")));
        assertTrue(predicate.test(new PersonBuilder().withSession("S1").build()));

        // Multiple sessions input match
        predicate = new SessionMatchPredicate(Arrays.asList(new Session("S1"), new Session("S2")));
        assertTrue(predicate.test(new PersonBuilder().withSession("S1").build()));
    }

    @Test
    public void test_personDoesNotExistInSession_returnsFalse() {
        // Non-matching session
        SessionMatchPredicate predicate = new SessionMatchPredicate(Arrays.asList(new Session("S1")));
        assertFalse(predicate.test(new PersonBuilder().withSession("S2").build()));
    }

    @Test
    public void toStringMethod() {
        List<Session> sessions = List.of(new Session("S1"), new Session("S2"));
        SessionMatchPredicate predicate = new SessionMatchPredicate(sessions);

        String expected = SessionMatchPredicate.class.getCanonicalName() + "{sessions=" + sessions + "}";
        assertEquals(expected, predicate.toString());
    }
}

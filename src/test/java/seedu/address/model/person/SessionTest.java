package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SessionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Session(null));
    }

    @Test
    public void constructor_invalidSession_throwsIllegalArgumentException() {
        String invalidSession = "";
        assertThrows(IllegalArgumentException.class, () -> new Session(invalidSession));
    }

    @Test
    public void isValidSession() {
        // null session
        assertThrows(NullPointerException.class, () -> Session.isValidSession(null));

        // invalid sessions
        assertFalse(Session.isValidSession("")); // empty string
        assertFalse(Session.isValidSession(" ")); // spaces only
        assertFalse(Session.isValidSession("g17")); // lowercase letter
        assertFalse(Session.isValidSession("17G")); // digits before letter
        assertFalse(Session.isValidSession("GG17")); // too many letters
        assertFalse(Session.isValidSession("G173")); // more than 2 digits
        assertFalse(Session.isValidSession("G1A")); // letter after digit
        assertFalse(Session.isValidSession("G01"));

        // valid sessions
        assertTrue(Session.isValidSession("G1")); // single digit
        assertTrue(Session.isValidSession("F7"));
        assertTrue(Session.isValidSession("E12"));
        assertTrue(Session.isValidSession("Z99"));
    }

    @Test
    public void equals() {
        Session session = new Session("G17");

        // same values -> returns true
        assertTrue(session.equals(new Session("G17")));

        // same object -> returns true
        assertTrue(session.equals(session));

        // null -> returns false
        assertFalse(session.equals(null));

        // different types -> returns false
        assertFalse(session.equals(5.0f));

        // different values -> returns false
        assertFalse(session.equals(new Session("F7")));
    }
}

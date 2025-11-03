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
        assertFalse(Session.isValidSession("G173")); // more than 2 digits

        // valid sessions
        assertTrue(Session.isValidSession("G1")); // single digit
        assertTrue(Session.isValidSession("F7"));
        assertTrue(Session.isValidSession("E12"));
        assertTrue(Session.isValidSession("Z99"));
    }


    @Test
    public void isValidSession_validFormats_true() {
        assertTrue(Session.isValidSession("G1"));
        assertTrue(Session.isValidSession("F01"));
        assertTrue(Session.isValidSession("T07"));
        assertTrue(Session.isValidSession("BA03"));
        assertTrue(Session.isValidSession("BD04"));
        assertTrue(Session.isValidSession("T10"));
        assertTrue(Session.isValidSession("T07B"));
        assertTrue(Session.isValidSession("AB99C"));
        assertTrue(Session.isValidSession("G9"));
        assertTrue(Session.isValidSession("G09"));
        assertTrue(Session.isValidSession("BA99"));
    }

    @Test
    public void isValidSession_invalidFormats_false() {
        assertFalse(Session.isValidSession("")); // empty
        assertFalse(Session.isValidSession("0"));
        assertFalse(Session.isValidSession("G0"));
        assertFalse(Session.isValidSession("G00")); // 00 not allowed
        assertFalse(Session.isValidSession("BA00"));
        assertFalse(Session.isValidSession("BA100")); // >99
        assertFalse(Session.isValidSession("bA03")); // lowercase
        assertFalse(Session.isValidSession("T7b")); // suffix must be uppercase
        assertFalse(Session.isValidSession("T7Z9")); // extra chars
        assertFalse(Session.isValidSession("A")); // missing digits
        assertFalse(Session.isValidSession("AAA07")); // prefix length>2
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

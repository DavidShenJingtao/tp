package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's session in TAConnect.
 * Format: 1–2 uppercase letters + number 1–99 (allow leading zero for 1–9) + optional trailing uppercase letter.
 * Examples: G1, F01, T07, BA03, BD04, T07B.
 * Guarantees: immutable; is valid as declared in {@link #isValidSession(String)}.
 */
public class Session {

    public static final String MESSAGE_CONSTRAINTS =
            "Sessions must be 1–2 uppercase letters, followed by 1–99 "
                    + "(leading zero allowed for 1–9), with an optional trailing uppercase letter "
                    + "(e.g., G1, F01, T07, BA03, BD04, T07B).";

    // 1–2 letters, then (1–9 with optional leading 0 OR 10–99), optional trailing letter
    public static final String VALIDATION_REGEX = "[A-Z]{1,2}(?:0?[1-9]|[1-9][0-9])(?:[A-Z])?";

    public final String value;

    /**
     * Constructs a {@code Session}.
     *
     * @param session A valid session number.
     */
    public Session(String session) {
        requireNonNull(session);
        checkArgument(isValidSession(session), MESSAGE_CONSTRAINTS);
        this.value = session;
    }

    /**
     * Returns true if the given string is a valid session identifier.
     * A valid session consists of 1–2 uppercase letters, followed by a number from 1–99
     * (leading zero allowed for 1–9), and an optional trailing uppercase letter.
     *
     * @param test String to validate.
     * @return True if the string matches the session format.
     */
    public static boolean isValidSession(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Session)) {
            return false;
        }

        Session otherSession = (Session) other;
        return value.equals(otherSession.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

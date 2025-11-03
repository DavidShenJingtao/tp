package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    /*
     * Allowed characters in legal names: letters (no digits), spaces, apostrophes (' or \u2019),
     * hyphens (-), periods (.), and slashes (/). The first character must not be whitespace
     * to prevent blank inputs.
     */
    public static final String VALIDATION_REGEX =
            "[-\\p{L}'\\u2018\\u2019\\u02BC./][-\\p{L}'\\u2018\\u2019\\u02BC./ ]*";
    public static final int MAX_NAME_LENGTH = 500;

    public static final String MESSAGE_CONSTRAINTS =
        "Names can contain letters, spaces, apostrophes (' or \u2019), hyphens (-),\n"
        + "periods (.), and slashes (/). They must not be blank and must be at most "
        + MAX_NAME_LENGTH + " characters.";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_NAME_LENGTH;
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}

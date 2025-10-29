package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only disallowed symbol
        assertFalse(Name.isValidName("peter*")); // contains disallowed symbol
        assertFalse(Name.isValidName("A".repeat(Name.MAX_NAME_LENGTH + 1))); // more than MAX_NAME_LENGTH letters

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertFalse(Name.isValidName("12345")); // numbers only not allowed
        assertFalse(Name.isValidName("peter the 2nd")); // digits not allowed
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr")); // long names without digits
        assertTrue(Name.isValidName("A".repeat(Name.MAX_NAME_LENGTH))); // equal MAX_NAME_LENGTH letters
        // newly-allowed legal symbols
        assertTrue(Name.isValidName("O'Connor")); // apostrophe
        assertTrue(Name.isValidName("D\u2019Angelo")); // typographic apostrophe (U+2019)
        assertTrue(Name.isValidName("Jean-Luc")); // hyphen
        assertTrue(Name.isValidName("J. P. Morgan")); // period and spaces
        assertTrue(Name.isValidName("Rajesh S/O Raman")); // slash with patronymic marker
        assertTrue(Name.isValidName("Meena D/O Kumar")); // slash with patronymic marker
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}

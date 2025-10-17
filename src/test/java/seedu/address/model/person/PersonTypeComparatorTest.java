package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Tests for PersonTypeComparator: student > ta > instructor > staff, then name A..Z.
 */
public class PersonTypeComparatorTest {

    private final PersonTypeComparator comparator = new PersonTypeComparator();

    @Test
    public void compare_rolePriority_orderedCorrectly() {
        Person student = new PersonBuilder().withName("X").withType("student").build();
        Person ta = new PersonBuilder().withName("X").withType("ta").build();
        Person instructor = new PersonBuilder().withName("X").withType("instructor").build();
        Person staff = new PersonBuilder().withName("X").withType("staff").build();

        // student before ta
        assertTrue(comparator.compare(student, ta) < 0);
        // ta before instructor
        assertTrue(comparator.compare(ta, instructor) < 0);
        // instructor before staff
        assertTrue(comparator.compare(instructor, staff) < 0);
        // student before staff
        assertTrue(comparator.compare(student, staff) < 0);
    }

    @Test
    public void compare_sameRole_alphabeticalByName() {
        Person a = new PersonBuilder().withName("Alice").withType("student").build();
        Person b = new PersonBuilder().withName("bob").withType("student").build(); // case-insensitive

        // "Alice" before "bob"
        assertTrue(comparator.compare(a, b) < 0);
        // symmetric
        assertTrue(comparator.compare(b, a) > 0);
        // equal names -> 0
        Person a2 = new PersonBuilder().withName("Alice").withType("student").build();
        assertTrue(comparator.compare(a, a2) == 0);
    }
}

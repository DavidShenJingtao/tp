package seedu.address.model.person;

import java.util.Comparator;

/**
 * Comparator for sorting Person by type priority (student > ta > instructor > staff),
 * then by alphabetical order of name.
 */
public class PersonTypeComparator implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        int rank1 = getRank(p1.getType());
        int rank2 = getRank(p2.getType());

        if (rank1 != rank2) {
            return Integer.compare(rank1, rank2);
        }

        // Same type -> compare by name
        return p1.getName().fullName.compareToIgnoreCase(p2.getName().fullName);
    }

    private int getRank(Type type) {
        if (type.isStudent()) {
            return 0;
        } else if (type.isTa()) {
            return 1;
        } else if (type.isInstructor()) {
            return 2;
        } else if (type.isStaff()) {
            return 3;
        } else {
            // In case new types added later
            return Integer.MAX_VALUE;
        }
    }
}

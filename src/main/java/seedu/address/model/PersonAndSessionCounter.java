package seedu.address.model;

import java.util.HashMap;
import java.util.List;

import seedu.address.model.person.Person;
import seedu.address.model.person.Session;

/**
 * Tracks total number of unique person and sessions
 */
public class PersonAndSessionCounter implements ReadOnlyPersonAndSessionCounter {
    private short personCount;
    private HashMap<Session, Short> individualSessionCount;
    private short uniqueSessionCount;


    public PersonAndSessionCounter() {
        individualSessionCount = new HashMap<>();
    }

    /**
     * Adds a person to the counter object.
     */
    public void add(Person p) {
        // Person count
        assert personCount < Short.MAX_VALUE; // no overflows
        personCount++;

        // Session count
        if (!p.getSession().isPresent()) {
            return;
        }

        Session session = p.getSession().get();
        if (individualSessionCount.containsKey(session)) {
            short currentCount = individualSessionCount.get(session);
            assert currentCount < Short.MAX_VALUE; // no overflows
            individualSessionCount.replace(session, (short) (currentCount + 1));
        } else {
            individualSessionCount.put(session, (short) 1);
            assert uniqueSessionCount < Short.MAX_VALUE; // no overflows
            uniqueSessionCount++;
        }
    }

    /**
     * Removes a person from the counter object.
     */
    public void remove(Person p) {
        // Person count
        assert personCount > 0;
        personCount--;

        // Session count
        if (!p.getSession().isPresent()) {
            return;
        }

        Session session = p.getSession().get();
        Short currentCount = individualSessionCount.get(session);
        assert currentCount != null && currentCount > 0;
        short newCount = (short) (currentCount - 1);
        if (newCount > 0) {
            individualSessionCount.replace(session, newCount);
        } else {
            individualSessionCount.remove(session);
            assert uniqueSessionCount > 0;
            uniqueSessionCount--;
        }
    }

    /**
     * Reset counter object with new list of persons
     */
    public void setPersons(List<Person> persons) {
        // clear current state
        personCount = 0;
        individualSessionCount.clear();
        uniqueSessionCount = 0;

        for (Person p : persons) {
            add(p); // reuse existing logic
        }
    }

    @Override
    public short getPersonCountIfPersonAdded() {
        return (short) (personCount + 1);
    }

    @Override
    public short getUniqueSessionCountIfSessionAdded(Session s) {
        return (individualSessionCount.get(s) == null) ? (short) (uniqueSessionCount + 1) : uniqueSessionCount;
    }
}

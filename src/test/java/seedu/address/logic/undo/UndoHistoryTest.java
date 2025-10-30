package seedu.address.logic.undo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

class UndoHistoryTest {

    @BeforeEach
    public void setUp() {
        UndoHistory.clear();
    }

    @Test
    public void recordState_exceedsLimit_evictsOldest() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        int totalStates = 55; // exceed limit (50)
        for (int i = 0; i < totalStates; i++) {
            AddressBook state = new AddressBook();
            String suffix = alphaIndex(i); // avoid digits in name
            Person person = new PersonBuilder().withName("Person " + suffix).build();
            state.addPerson(person);
            UndoHistory.recordState(state, "cmd" + i);
        }

        // Only the most recent 50 entries (cmd5 ... cmd54) should remain.
        for (int offset = 0; offset < 50; offset++) {
            String expectedLabel = "cmd" + (totalStates - 1 - offset);
            String restoredLabel = UndoHistory.restorePreviousState(model);
            assertEquals(expectedLabel, restoredLabel);

            String expectedPersonName = "Person " + alphaIndex(totalStates - 1 - offset);
            assertEquals(expectedPersonName,
                    model.getAddressBook().getPersonList().get(0).getName().fullName);
        }

        assertFalse(UndoHistory.canUndo());
    }

    @Test
    public void restorePreviousState_returnsNullWhenEmpty() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        assertFalse(UndoHistory.canUndo());
        assertEquals(null, UndoHistory.restorePreviousState(model));
    }

    @Test
    public void recordState_thenRestore_returnsLatestLabel() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        AddressBook state = new AddressBook();
        state.addPerson(new PersonBuilder().withName("Alice").build());

        UndoHistory.recordState(state, "export");
        assertTrue(UndoHistory.canUndo());

        String label = UndoHistory.restorePreviousState(model);
        assertEquals("export", label);
        assertEquals("Alice", model.getAddressBook().getPersonList().get(0).getName().fullName);
    }

    // Returns a two-letter uppercase index (AA, AB, ..., AZ, BA, ...) for small n (sufficient for this test)
    private String alphaIndex(int n) {
        int a = n / 26;
        int b = n % 26;
        return "" + (char) ('A' + a) + (char) ('A' + b);
    }
}

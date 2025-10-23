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
            Person person = new PersonBuilder().withName("Person " + i).build();
            state.addPerson(person);
            UndoHistory.recordState(state, "cmd" + i);
        }

        // Only the most recent 50 entries (cmd5 ... cmd54) should remain.
        for (int offset = 0; offset < 50; offset++) {
            String expectedLabel = "cmd" + (totalStates - 1 - offset);
            String restoredLabel = UndoHistory.restorePreviousState(model);
            assertEquals(expectedLabel, restoredLabel);

            String expectedPersonName = "Person " + (totalStates - 1 - offset);
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
}


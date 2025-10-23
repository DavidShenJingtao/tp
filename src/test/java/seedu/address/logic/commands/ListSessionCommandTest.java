package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Session;
import seedu.address.model.person.SessionMatchPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code ListSessionCommand}.
 */
public class ListSessionCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SessionMatchPredicate firstPredicate =
                new SessionMatchPredicate(Collections.singletonList(new Session("G1")));
        SessionMatchPredicate secondPredicate =
                new SessionMatchPredicate(Collections.singletonList(new Session("F12")));

        ListSessionCommand listSessionFirstCommand = new ListSessionCommand(firstPredicate);
        ListSessionCommand listSessionSecondCommand = new ListSessionCommand(secondPredicate);

        // same object -> returns true
        assertTrue(listSessionFirstCommand.equals(listSessionFirstCommand));

        // same values -> returns true
        ListSessionCommand listSessionFirstCommandCopy = new ListSessionCommand(firstPredicate);
        assertTrue(listSessionFirstCommand.equals(listSessionFirstCommandCopy));

        // different types -> returns false
        assertFalse(listSessionFirstCommand.equals(1));

        // null -> returns false
        assertFalse(listSessionFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(listSessionFirstCommand.equals(listSessionSecondCommand));
    }

    @Test
    public void execute_zeroSessionMatches_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        // S0 is no longer accepted -> change to S99
        SessionMatchPredicate predicate = preparePredicate("S99"); // was "S0"
        ListSessionCommand command = new ListSessionCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }


    @Test
    public void execute_multipleSessions_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        SessionMatchPredicate predicate = preparePredicate("S1 S2");
        ListSessionCommand command = new ListSessionCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        SessionMatchPredicate predicate = new SessionMatchPredicate(Arrays.asList(new Session("S1")));
        ListSessionCommand listSessionCommand = new ListSessionCommand(predicate);
        String expected = ListSessionCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, listSessionCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code SessionMatchPredicate}.
     */
    private SessionMatchPredicate preparePredicate(String userInput) {
        String[] stringSessions = userInput.split("\\s+");
        final List<Session> sessions = new ArrayList<>(stringSessions.length);
        for (int i = 0; i < stringSessions.length; i++) {
            String session = stringSessions[i];
            sessions.add(new Session(session));
        }

        return new SessionMatchPredicate(sessions);
    }
}

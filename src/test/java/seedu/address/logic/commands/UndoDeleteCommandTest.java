package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand.Selector;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class UndoDeleteCommandTest {

    private Model model;

    @BeforeEach
    public void setup() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        DeleteUndoBuffer.clear();
    }

    @Test
    public void execute_nothingToUndo_failure() {
        assertCommandFailure(new UndoDeleteCommand(), model, UndoDeleteCommand.MESSAGE_NOTHING_TO_UNDO);
    }

    @Test
    public void execute_singleDelete_undoSuccess() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand delete = new DeleteCommand(List.of(Selector.fromIndex(INDEX_FIRST_PERSON)));

        String deleteMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(first));
        Model expectedAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(first);
        assertCommandSuccess(delete, model, deleteMessage, expectedAfterDelete);

        // Now undo; expected model is previous (deleted) state with the person appended back
        Model expectedAfterUndo = new ModelManager(expectedAfterDelete.getAddressBook(), new UserPrefs());
        expectedAfterUndo.addPerson(first);
        UndoDeleteCommand undo = new UndoDeleteCommand();
        assertCommandSuccess(undo, model,
                String.format(UndoDeleteCommand.MESSAGE_UNDO_DELETE_PERSON_SUCCESS, Messages.format(first)),
                expectedAfterUndo);
    }

    @Test
    public void execute_multiDelete_undoSuccess() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person second = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand delete = new DeleteCommand(List.of(Selector.fromIndex(INDEX_FIRST_PERSON),
                Selector.fromIndex(INDEX_SECOND_PERSON)));

        String deleteMessage = String.format(DeleteCommand.MESSAGE_DELETE_MULTIPLE_PERSON_SUCCESS,
                Messages.format(first) + "\n" + Messages.format(second));
        Model expectedAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(first);
        expectedAfterDelete.deletePerson(second);
        assertCommandSuccess(delete, model, deleteMessage, expectedAfterDelete);

        // Now undo; expected model is previous (deleted) state with persons appended back in order
        Model expectedAfterUndo = new ModelManager(expectedAfterDelete.getAddressBook(), new UserPrefs());
        expectedAfterUndo.addPerson(first);
        expectedAfterUndo.addPerson(second);
        UndoDeleteCommand undo = new UndoDeleteCommand();
        String expectedUndoMsg = String.format(UndoDeleteCommand.MESSAGE_UNDO_DELETE_MULTIPLE_PERSON_SUCCESS,
                Messages.format(first) + "\n" + Messages.format(second));
        assertCommandSuccess(undo, model, expectedUndoMsg, expectedAfterUndo);
    }
}

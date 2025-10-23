package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.undo.UndoHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains integration tests for {@code UndoCommand}.
 */
public class UndoCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        UndoHistory.clear();
    }

    @Test
    public void execute_noHistory_throwsCommandException() {
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NOTHING_TO_UNDO);
    }

    @Test
    public void execute_afterDeleteCommand_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        DeleteCommand deleteCommand = new DeleteCommand(
                List.of(DeleteCommand.Selector.fromIndex(Index.fromOneBased(1))));

        Model expectedAfterDelete = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(firstPerson);
        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(firstPerson)),
                expectedAfterDelete);

        Model expectedAfterUndo = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedAfterUndo);
    }

    @Test
    public void execute_afterMultipleMutations_restoresLatest() {
        Person newPerson = new PersonBuilder().withName("Zara Tan").build();

        // add new person
        AddCommand addCommand = new AddCommand(newPerson);
        Model expectedAfterAdd = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        expectedAfterAdd.addPerson(newPerson);
        assertCommandSuccess(addCommand, model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(newPerson)), expectedAfterAdd);

        // delete first person
        Person firstPerson = model.getFilteredPersonList().get(0);
        DeleteCommand deleteCommand = new DeleteCommand(
                List.of(DeleteCommand.Selector.fromIndex(Index.fromOneBased(1))));
        Model expectedAfterDelete = new ModelManager(expectedAfterAdd.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(firstPerson);
        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(firstPerson)),
                expectedAfterDelete);

        // undo should restore state before delete (i.e. after add)
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS,
                new ModelManager(expectedAfterAdd.getAddressBook(), new UserPrefs()));
    }

    @Test
    public void execute_afterClearCommand_success() {
        ClearCommand clearCommand = new ClearCommand();
        Model expectedAfterClear = new ModelManager(new AddressBook(), new UserPrefs());
        assertCommandSuccess(clearCommand, model, ClearCommand.MESSAGE_SUCCESS, expectedAfterClear);

        Model expectedAfterUndo = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedAfterUndo);
    }
}

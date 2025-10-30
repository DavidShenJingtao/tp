package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Tests for export helper logic in {@link MainWindow} without requiring JavaFX.
 */
public class MainWindowExportHelperTest {

    private static final Logger LOGGER = Logger.getLogger(MainWindowExportHelperTest.class.getName());

    @Test
    public void getExportFeedback_success_returnsMessage() throws Exception {
        StubLogic logic = new StubLogic(new CommandResult("Exported 1 contact(s) to exports/sample.csv"), null);

        String feedback = MainWindow.getExportFeedback(logic, LOGGER);

        assertEquals("Exported 1 contact(s) to exports/sample.csv", feedback);
    }

    @Test
    public void getExportFeedback_failure_returnsErrorMessage() throws Exception {
        StubLogic logic = new StubLogic(null, new CommandException(ExportCommand.MESSAGE_NO_CONTACTS));

        String feedback = MainWindow.getExportFeedback(logic, LOGGER);

        assertEquals(ExportCommand.MESSAGE_NO_CONTACTS, feedback);
    }

    private static class StubLogic implements Logic {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final AddressBook addressBook = new AddressBook();

        private final CommandResult exportResult;
        private final CommandException exportException;

        private StubLogic(CommandResult exportResult, CommandException exportException) {
            this.exportResult = exportResult;
            this.exportException = exportException;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CommandResult exportDisplayedContacts() throws CommandException {
            if (exportException != null) {
                throw exportException;
            }
            return exportResult;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("data", "addressbook.json");
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // No-op for tests.
        }
    }
}

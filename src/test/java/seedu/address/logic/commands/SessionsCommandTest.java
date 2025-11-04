package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.storage.JsonAddressBookStorage;

public class SessionsCommandTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "SessionsCommandTest");
    private static final Path MANY_SESSIONS_FILE = TEST_DATA_FOLDER.resolve("manySessionsAddressBook.json");

    @Test
    public void execute_sessions_success() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(MANY_SESSIONS_FILE);
        AddressBook addressBook = new AddressBook();
        try {
            addressBook = (AddressBook) addressBookStorage.readAddressBook().get();
        } catch (DataLoadingException e) {
            fail();
        }
        ModelStub modelStub = new ModelStubWithSuppliedAddressBook(addressBook);
        Set<Session> expectedSessions = addressBook.getCounter().listAllSessions();
        CommandResult expectedCommandResult = new CommandResult(String.format(
            SessionsCommand.MESSAGE_LIST_ALL_SESSIONS, expectedSessions.size(),
            expectedSessions.stream().map(Session::toString)
                    .sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(", "))
        ));
        assertCommandSuccess(new SessionsCommand(), modelStub, expectedCommandResult, modelStub);
    }

    @Test
    public void execute_noSessionsFound_success() {
        AddressBook addressBook = new AddressBook();
        ModelStub modelStub = new ModelStubWithSuppliedAddressBook(addressBook);
        CommandResult expectedCommandResult = new CommandResult(SessionsCommand.MESSAGE_NO_SESSIONS_FOUND);
        assertCommandSuccess(new SessionsCommand(), modelStub, expectedCommandResult, modelStub);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) { /* no-op */ }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains supplied address book.
     */
    private class ModelStubWithSuppliedAddressBook extends ModelStub {
        private final AddressBook addressBook;

        ModelStubWithSuppliedAddressBook(AddressBook addressBook) {
            this.addressBook = addressBook;
        }


        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return this.addressBook;
        }
    }
}

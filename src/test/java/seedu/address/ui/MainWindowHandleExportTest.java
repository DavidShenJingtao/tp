package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import sun.reflect.ReflectionFactory;

/**
 * Tests {@link MainWindow#handleExport()} via reflection without initializing JavaFX.
 */
public class MainWindowHandleExportTest {

    private static final Logger LOGGER = Logger.getLogger(MainWindowHandleExportTest.class.getName());

    @Test
    public void handleExport_success_setsFeedback() throws Exception {
        CommandResult expected = new CommandResult("Exported 1 contact(s) to exports/sample.csv");
        LogicStub logicStub = new LogicStub(expected, null);
        ResultDisplayStub displayStub = ResultDisplayStub.create();
        MainWindow mainWindow = createMainWindowInstance(logicStub, displayStub);

        invokeHandleExport(mainWindow);

        assertEquals(expected.getFeedbackToUser(), displayStub.getMessage());
    }

    @Test
    public void handleExport_failure_setsErrorFeedback() throws Exception {
        CommandException failure = new CommandException(ExportCommand.MESSAGE_NO_CONTACTS);
        LogicStub logicStub = new LogicStub(null, failure);
        ResultDisplayStub displayStub = ResultDisplayStub.create();
        MainWindow mainWindow = createMainWindowInstance(logicStub, displayStub);

        invokeHandleExport(mainWindow);

        assertEquals(ExportCommand.MESSAGE_NO_CONTACTS, displayStub.getMessage());
    }

    private static MainWindow createMainWindowInstance(Logic logic, ResultDisplayStub display) throws Exception {
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor<Object> objectConstructor = Object.class.getDeclaredConstructor();
        Constructor<?> serializationConstructor =
                reflectionFactory.newConstructorForSerialization(MainWindow.class, objectConstructor);
        serializationConstructor.setAccessible(true);
        MainWindow instance = (MainWindow) serializationConstructor.newInstance();

        setField(instance, "logic", logic);
        setField(instance, "resultDisplay", display);
        setField(instance, "logger", LOGGER);
        return instance;
    }

    private static void invokeHandleExport(MainWindow mainWindow) throws Exception {
        Method method = MainWindow.class.getDeclaredMethod("handleExport");
        method.setAccessible(true);
        method.invoke(mainWindow);
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = MainWindow.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static class ResultDisplayStub extends ResultDisplay {
        private String message;

        private ResultDisplayStub() {
            // Prevent super constructor from running.
        }

        static ResultDisplayStub create() throws Exception {
            ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
            Constructor<Object> objectConstructor = Object.class.getDeclaredConstructor();
            Constructor<?> serializationConstructor =
                    reflectionFactory.newConstructorForSerialization(ResultDisplayStub.class, objectConstructor);
            serializationConstructor.setAccessible(true);
            return (ResultDisplayStub) serializationConstructor.newInstance();
        }

        @Override
        public void setFeedbackToUser(String feedbackToUser) {
            this.message = feedbackToUser;
        }

        String getMessage() {
            return message;
        }
    }

    private static class LogicStub implements Logic {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final AddressBook addressBook = new AddressBook();
        private final CommandResult exportResult;
        private final CommandException exportException;

        private LogicStub(CommandResult exportResult, CommandException exportException) {
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
            // No-op
        }
    }
}

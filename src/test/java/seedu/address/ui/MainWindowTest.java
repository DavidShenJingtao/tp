package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Lightweight tests for {@link MainWindow} covering the export button behaviour without TestFX.
 */
public class MainWindowTest {

    private static final Person SAMPLE_PERSON = new PersonBuilder().build();

    private MainWindow mainWindow;
    private LogicStub logicStub;
    private Stage stage;

    @BeforeAll
    static void initFx() {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Skipping JavaFX tests in headless environment");
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    @BeforeEach
    void setUp() {
        logicStub = new LogicStub();
        runFxAndWait(() -> {
            stage = new Stage();
            mainWindow = new MainWindow(stage, logicStub);
            mainWindow.fillInnerParts();
        });
    }

    @AfterEach
    void tearDown() {
        runFxAndWait(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    @Test
    public void handleExport_success_updatesResultDisplay() {
        CommandResult expected = new CommandResult("Exported 1 contact(s) to exports/sample.csv");
        logicStub.setExportResult(expected);

        invokeHandleExport();

        assertEquals(expected.getFeedbackToUser(), getResultDisplayText());
    }

    @Test
    public void handleExport_failure_showsErrorMessage() {
        logicStub.setExportFailure(new CommandException("There are no contacts to export."));

        invokeHandleExport();

        assertEquals("There are no contacts to export.", getResultDisplayText());
    }

    private void invokeHandleExport() {
        runFxAndWait(() -> {
            try {
                Method method = MainWindow.class.getDeclaredMethod("handleExport");
                method.setAccessible(true);
                method.invoke(mainWindow);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
    }

    private String getResultDisplayText() {
        final String[] textHolder = new String[1];
        runFxAndWait(() -> {
            try {
                Field field = MainWindow.class.getDeclaredField("resultDisplay");
                field.setAccessible(true);
                ResultDisplay display = (ResultDisplay) field.get(mainWindow);
                Field textAreaField = ResultDisplay.class.getDeclaredField("resultDisplay");
                textAreaField.setAccessible(true);
                textHolder[0] = ((javafx.scene.control.TextArea) textAreaField.get(display)).getText();
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
        return textHolder[0];
    }

    private static void runFxAndWait(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new AssertionError("Timed out waiting for FX task");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
    }

    private static class LogicStub implements Logic {
        private final ObservableList<Person> persons = FXCollections.observableArrayList(SAMPLE_PERSON);
        private final AddressBook addressBook = createAddressBook(persons);

        private CommandResult exportResult = new CommandResult("ok");
        private CommandException exportException;

        void setExportResult(CommandResult result) {
            this.exportResult = result;
            this.exportException = null;
        }

        void setExportFailure(CommandException exception) {
            this.exportException = exception;
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
            // no-op
        }

        private static AddressBook createAddressBook(ObservableList<Person> persons) {
            AddressBook addressBook = new AddressBook();
            persons.forEach(addressBook::addPerson);
            return addressBook;
        }
    }
}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    @TempDir
    Path tempDir;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(new PersonBuilder()
                .withName("Alice Pauline")
                .withTelegram("@alice123")
                .withEmail("alice@example.com")
                .withSession("G1")
                .build());
        model.addPerson(new PersonBuilder()
                .withName("Bob Brown")
                .withTelegram(null)
                .withEmail("bob@example.com")
                .withSession("G2")
                .build());
    }

    @Test
    public void execute_specifiedFile_exportsCsv() throws Exception {
        Path targetFile = tempDir.resolve("team.csv");
        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        ExportCommand exportCommand = new ExportCommand(targetFile, false, () -> fixedTime);

        CommandResult result = exportCommand.execute(model);

        assertTrue(Files.exists(targetFile));
        List<String> lines = Files.readAllLines(targetFile);
        assertEquals(List.of(
                "Name,Telegram,Email,Type,Session",
                "\"Alice Pauline\",\"@alice123\",\"alice@example.com\",\"student\",\"G1\"",
                "\"Bob Brown\",\"\",\"bob@example.com\",\"student\",\"G2\""), lines);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, targetFile.toAbsolutePath()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_directoryProvided_createsTimestampedFile() throws Exception {
        Path directory = tempDir.resolve("exports");
        LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 1, 9, 30, 15);
        ExportCommand exportCommand = new ExportCommand(directory, true, () -> fixedTime);

        CommandResult result = exportCommand.execute(model);

        Path expectedFile = directory.resolve("contacts-20240201-093015.csv");
        assertTrue(Files.exists(expectedFile));
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, expectedFile.toAbsolutePath()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_noContacts_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Path targetFile = tempDir.resolve("empty.csv");
        ExportCommand exportCommand = new ExportCommand(targetFile, false, () -> LocalDateTime.now());

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(emptyModel));
        assertEquals(ExportCommand.MESSAGE_NO_CONTACTS, thrown.getMessage());
    }

    @Test
    public void equals() {
        Path samplePath = Paths.get("sample.csv");
        ExportCommand defaultCommand = new ExportCommand();
        ExportCommand samePathCommand = new ExportCommand(samplePath);
        ExportCommand samePathCommandCopy = new ExportCommand(samplePath);
        ExportCommand directoryCommand = new ExportCommand(Paths.get("exports"), true);

        // same object -> true
        assertTrue(defaultCommand.equals(defaultCommand));

        // same values -> true
        assertTrue(samePathCommand.equals(samePathCommandCopy));

        // different types -> false
        assertFalse(samePathCommand.equals(1));

        // null -> false
        assertFalse(samePathCommand.equals(null));

        // different configuration -> false
        assertFalse(samePathCommand.equals(directoryCommand));
    }
}

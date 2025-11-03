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
                "Name,Phone,Telegram,Email,Type,Session",
                "\"Alice Pauline\",\"85355255\",\"@alice123\",\"alice@example.com\",\"student\",\"G1\"",
                "\"Bob Brown\",\"85355255\",\"\",\"bob@example.com\",\"student\",\"G2\""), lines);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, targetFile.toAbsolutePath()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_defaultDirectory_createsTimestampedFile() throws Exception {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 3, 3, 15, 45, 30);
        Path expectedDirectory = Paths.get("exports");
        Path expectedFile = expectedDirectory.resolve("contacts-20240303-154530.csv");
        Files.deleteIfExists(expectedFile);

        ExportCommand exportCommand = new ExportCommand(null, false, () -> fixedTime);
        CommandResult result = exportCommand.execute(model);

        assertTrue(Files.exists(expectedFile));
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, expectedFile.toAbsolutePath()),
                result.getFeedbackToUser());

        Files.deleteIfExists(expectedFile);
        if (Files.exists(expectedDirectory)) {
            try (var stream = Files.list(expectedDirectory)) {
                if (!stream.findAny().isPresent()) {
                    Files.delete(expectedDirectory);
                }
            }
        }
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
    public void execute_relativeTargetWithoutParent_exportsCsv() throws Exception {
        Path relativeTarget = Paths.get("relative-export.csv");
        Files.deleteIfExists(relativeTarget);
        LocalDateTime fixedTime = LocalDateTime.of(2024, 7, 1, 8, 0);
        ExportCommand exportCommand = new ExportCommand(relativeTarget, false, () -> fixedTime);

        try {
            CommandResult result = exportCommand.execute(model);
            assertTrue(Files.exists(relativeTarget));
            assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, relativeTarget.toAbsolutePath()),
                    result.getFeedbackToUser());
        } finally {
            Files.deleteIfExists(relativeTarget);
        }
    }

    @Test
    public void execute_targetFileAlreadyExists_throwsCommandException() throws Exception {
        Path targetFile = tempDir.resolve("duplicate.csv");
        Files.createFile(targetFile);
        ExportCommand exportCommand = new ExportCommand(targetFile, false, () -> LocalDateTime.now());

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(String.format(ExportCommand.MESSAGE_IO_ERROR,
                "File already exists: " + targetFile.toAbsolutePath()), thrown.getMessage());
    }

    @Test
    public void execute_targetFileAlreadyExistsOverwriteEnabled_succeeds() throws Exception {
        Path targetFile = tempDir.resolve("overwrite.csv");
        Files.createDirectories(targetFile.getParent());
        Files.writeString(targetFile, "existing");

        LocalDateTime fixedTime = LocalDateTime.of(2024, 6, 1, 10, 0);
        ExportCommand exportCommand = new ExportCommand(targetFile, false, () -> fixedTime, true);

        CommandResult result = exportCommand.execute(model);

        List<String> lines = Files.readAllLines(targetFile);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, targetFile.toAbsolutePath()),
                result.getFeedbackToUser());
        assertEquals(List.of(
                "Name,Phone,Telegram,Email,Type,Session",
                "\"Alice Pauline\",\"85355255\",\"@alice123\",\"alice@example.com\",\"student\",\"G1\"",
                "\"Bob Brown\",\"85355255\",\"\",\"bob@example.com\",\"student\",\"G2\""), lines);
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
        ExportCommand overwriteCommand = new ExportCommand(samplePath, false, true);

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

        // different overwrite flag -> false
        assertFalse(samePathCommand.equals(overwriteCommand));
    }
}

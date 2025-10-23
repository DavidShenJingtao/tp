package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Supplier;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports the currently displayed contacts to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the currently shown contacts to a CSV file. "
            + "Optionally provide a target path, e.g. \"export exports/my-class.csv\".";

    public static final String MESSAGE_SUCCESS = "Exported %d contact(s) to %s";
    public static final String MESSAGE_NO_CONTACTS = "There are no contacts to export.";
    public static final String MESSAGE_IO_ERROR = "Unable to export contacts: %s";

    private static final Path DEFAULT_EXPORT_DIRECTORY = Paths.get("exports");
    private static final DateTimeFormatter FILE_NAME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path targetPath;
    private final boolean treatTargetAsDirectory;
    private final Supplier<LocalDateTime> timestampSupplier;

    /**
     * Creates an {@code ExportCommand} that writes to a timestamped CSV file inside the default export directory.
     */
    public ExportCommand() {
        this(null, false);
    }

    /**
     * Creates an {@code ExportCommand} that writes to {@code targetPath}.
     */
    public ExportCommand(Path targetPath) {
        this(targetPath, false);
    }

    /**
     * Creates an {@code ExportCommand} that writes into {@code targetPath} treating it explicitly as a directory.
     */
    public ExportCommand(Path targetPath, boolean treatAsDirectory) {
        this(targetPath, treatAsDirectory, LocalDateTime::now);
    }

    /**
     * Internal constructor that allows injecting a timestamp supplier for testing.
     */
    ExportCommand(Path targetPath, boolean treatAsDirectory, Supplier<LocalDateTime> timestampSupplier) {
        this.targetPath = targetPath;
        this.treatTargetAsDirectory = treatAsDirectory;
        this.timestampSupplier = requireNonNull(timestampSupplier);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> personsToExport = model.getFilteredPersonList();
        if (personsToExport.isEmpty()) {
            throw new CommandException(MESSAGE_NO_CONTACTS);
        }

        LocalDateTime exportTime = timestampSupplier.get();
        Path resolvedPath;
        try {
            resolvedPath = resolveExportPath(targetPath, exportTime);
            writeCsv(resolvedPath, personsToExport);
        } catch (IOException ioe) {
            throw new CommandException(String.format(MESSAGE_IO_ERROR, ioe.getMessage()), ioe);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                personsToExport.size(), resolvedPath.toAbsolutePath()));
    }

    private Path resolveExportPath(Path userProvidedPath, LocalDateTime timestamp) throws IOException {
        if (userProvidedPath == null) {
            Path directory = DEFAULT_EXPORT_DIRECTORY.toAbsolutePath().normalize();
            Files.createDirectories(directory);
            return directory.resolve(buildFileName(timestamp));
        }

        Path resolved = userProvidedPath.toAbsolutePath().normalize();

        if (treatTargetAsDirectory || Files.isDirectory(resolved)) {
            Files.createDirectories(resolved);
            return resolved.resolve(buildFileName(timestamp));
        }

        Path parent = resolved.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return resolved;
    }

    private String buildFileName(LocalDateTime timestamp) {
        return "contacts-" + FILE_NAME_FORMAT.format(timestamp) + ".csv";
    }

    private void writeCsv(Path path, Iterable<Person> persons) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            writer.write("Name,Telegram,Email,Type,Session");
            writer.newLine();
            for (Person person : persons) {
                writer.write(String.join(",",
                        toCsvField(person.getName().fullName),
                        toCsvField(person.getTelegramUsername().map(Object::toString).orElse("")),
                        toCsvField(person.getEmail().value),
                        toCsvField(person.getType().toString()),
                        toCsvField(person.getSession().map(Object::toString).orElse(""))));
                writer.newLine();
            }
        } catch (FileAlreadyExistsException fileAlreadyExistsException) {
            throw new IOException("File already exists: " + path.toAbsolutePath(), fileAlreadyExistsException);
        }
    }

    private String toCsvField(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return Objects.equals(targetPath, otherExportCommand.targetPath)
                && treatTargetAsDirectory == otherExportCommand.treatTargetAsDirectory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetPath, treatTargetAsDirectory);
    }
}

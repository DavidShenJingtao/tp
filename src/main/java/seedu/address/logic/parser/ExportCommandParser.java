package seedu.address.logic.parser;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ExportCommand} object.
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    private static final String MESSAGE_INVALID_PATH = "The provided export path is invalid: %s";

    @Override
    public ExportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new ExportCommand();
        }

        try {
            Path path = Paths.get(trimmedArgs);
            boolean looksLikeDirectory = trimmedArgs.endsWith("/") || trimmedArgs.endsWith("\\");
            boolean treatAsDirectory = looksLikeDirectory || path.toFile().isDirectory();

            if (!treatAsDirectory && !trimmedArgs.toLowerCase().endsWith(".csv")) {
                path = path.resolveSibling(path.getFileName() + ".csv");
            }

            return new ExportCommand(path, treatAsDirectory);
        } catch (InvalidPathException invalidPathException) {
            throw new ParseException(String.format(MESSAGE_INVALID_PATH, trimmedArgs), invalidPathException);
        }
    }
}

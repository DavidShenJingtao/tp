package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UndoDeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the undo delete command.
 */
public class UndoDeleteCommandParser implements Parser<UndoDeleteCommand> {

    @Override
    public UndoDeleteCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            // allow plain "undo" to target last delete
            return new UndoDeleteCommand();
        }

        if (trimmed.equals("delete") || trimmed.equals("del") || trimmed.equals("rm")) {
            return new UndoDeleteCommand();
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoDeleteCommand.MESSAGE_USAGE));
    }
}


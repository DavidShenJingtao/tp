package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_SESSION_FORMAT;

import seedu.address.logic.commands.ListSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Session;
import seedu.address.model.person.SessionMatchPredicate;

/**
 * Parses input arguments and creates a new ListSessionCommand object
 */
public class ListSessionCommandParser implements Parser<ListSessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListSessionCommand
     * and returns a ListSessionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListSessionCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));
        }

        if (!Session.isValidSession(trimmedArgs)) {
            throw new ParseException(MESSAGE_INVALID_SESSION_FORMAT);
        }

        final Session session = new Session(trimmedArgs);
        return new ListSessionCommand(new SessionMatchPredicate(session));
    }

}

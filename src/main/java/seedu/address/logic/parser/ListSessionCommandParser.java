package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

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

        String[] stringSessions = trimmedArgs.split("\\s+");
        final List<Session> sessions = new ArrayList<>(stringSessions.length);

        for (int i = 0; i < stringSessions.length; i++) {
            String session = stringSessions[i];
            if (!Session.isValidSession(session)) {
                // todo(haxatron): More descriptive error
                throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));
            }
            sessions.add(new Session(session));
        }

        return new ListSessionCommand(new SessionMatchPredicate(sessions));
    }

}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.Selector;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final Prefix PREFIX_NAME = new Prefix("n:");
    private static final String USAGE = seedu.address.logic.commands.DeleteCommand.MESSAGE_USAGE
            .replace("n/", "n:");

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        List<Selector> selectors = new ArrayList<>();

        parseIndexes(argMultimap.getPreamble(), selectors);
        parseNames(argMultimap.getAllValues(PREFIX_NAME), selectors);

        if (selectors.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE));
        }

        return new DeleteCommand(selectors);
    }

    private void parseIndexes(String preamble, List<Selector> selectors) throws ParseException {
        if (preamble == null || preamble.isBlank()) {
            return;
        }

        String[] tokens = preamble.trim().split("\\s+");

        for (String token : tokens) {
            if (token.isBlank()) {
                continue;
            }
            // Support ranges like 2-5
            if (token.contains("-")) {
                String[] parts = token.split("-");
                if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE));
                }
                Index start;
                Index end;
                try {
                    start = ParserUtil.parseIndex(parts[0]);
                    end = ParserUtil.parseIndex(parts[1]);
                } catch (ParseException pe) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE), pe);
                }
                if (start.getOneBased() > end.getOneBased()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE));
                }
                for (int i = start.getOneBased(); i <= end.getOneBased(); i++) {
                    selectors.add(Selector.fromIndex(Index.fromOneBased(i)));
                }
                continue;
            }

            try {
                Index index = ParserUtil.parseIndex(token);
                selectors.add(Selector.fromIndex(index));
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE), pe);
            }
        }
    }

    private void parseNames(List<String> nameValues, List<Selector> selectors) throws ParseException {
        for (String rawName : nameValues) {
            String trimmedName = rawName.trim();
            if (trimmedName.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE));
            }
            try {
                Name name = ParserUtil.parseName(trimmedName);
                selectors.add(Selector.fromName(name));
            } catch (ParseException pe) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, USAGE), pe);
            }
        }
    }

}

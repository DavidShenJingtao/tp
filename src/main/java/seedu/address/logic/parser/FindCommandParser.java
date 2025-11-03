package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EMPTY_FIND_KEYWORD;
import static seedu.address.logic.Messages.MESSAGE_INVALID_FIND_KEYWORD;

import java.util.Collections;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    // Allow ASCII letters (no digits), spaces, apostrophes (' ’ ʼ), hyphens (-), periods (.), and slashes (/)
    private static final Pattern VALID_KEYWORD_PATTERN =
            Pattern.compile("[-A-Za-z .'/\\u2018\\u2019\\u02BC/]+");

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_FIND_KEYWORD);
        }

        String normalisedKeyword = trimmedArgs.replaceAll("\\s+", " ");

        if (!VALID_KEYWORD_PATTERN.matcher(normalisedKeyword).matches()) {
            throw new ParseException(MESSAGE_INVALID_FIND_KEYWORD);
        }

        return new FindCommand(new NameContainsKeywordsPredicate(Collections.singletonList(normalisedKeyword)));
    }

}

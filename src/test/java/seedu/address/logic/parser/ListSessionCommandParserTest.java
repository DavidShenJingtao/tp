package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListSessionCommand;
import seedu.address.model.person.Session;
import seedu.address.model.person.SessionMatchPredicate;

public class ListSessionCommandParserTest {

    private ListSessionCommandParser parser = new ListSessionCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidSession_throwsParseException() {
        // incorrect format
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));

        // more than 2 digits
        assertParseFailure(parser, "G100",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));

        // no capital letter
        assertParseFailure(parser, "g10",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsListSessionCommand() {
        // no leading and trailing whitespaces
        ListSessionCommand expectedListSessionCommand =
                new ListSessionCommand(new SessionMatchPredicate(new Session("G1")));
        assertParseSuccess(parser, "G1", expectedListSessionCommand);

        // multiple whitespaces between session
        assertParseSuccess(parser, " \n G1 \n", expectedListSessionCommand);
    }
}

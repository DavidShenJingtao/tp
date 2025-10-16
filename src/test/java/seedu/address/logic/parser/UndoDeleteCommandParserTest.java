package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoDeleteCommand;

public class UndoDeleteCommandParserTest {

    private final UndoDeleteCommandParser parser = new UndoDeleteCommandParser();

    @Test
    public void parse_emptyArgs_returnsUndo() {
        assertParseSuccess(parser, "", new UndoDeleteCommand());
        assertParseSuccess(parser, "   ", new UndoDeleteCommand());
    }

    @Test
    public void parse_validVariants_returnsUndo() {
        assertParseSuccess(parser, "delete", new UndoDeleteCommand());
        assertParseSuccess(parser, "del", new UndoDeleteCommand());
        assertParseSuccess(parser, "rm", new UndoDeleteCommand());
    }

    @Test
    public void parse_invalidArgs_failure() {
        assertParseFailure(parser, "something",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoDeleteCommand.MESSAGE_USAGE));
    }
}


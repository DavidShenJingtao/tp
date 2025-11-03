package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.Selector;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validIndexArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, " 1", new DeleteCommand(List.of(Selector.fromIndex(INDEX_FIRST_PERSON))));
    }

    @Test
    public void parse_multipleValidIndexArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, " 1 2", new DeleteCommand(List.of(
                Selector.fromIndex(INDEX_FIRST_PERSON), Selector.fromIndex(INDEX_SECOND_PERSON))));
    }

    @Test
    public void parse_validRange_returnsDeleteCommand() {
        assertParseSuccess(parser, " 1-2", new DeleteCommand(List.of(
                Selector.fromIndex(INDEX_FIRST_PERSON), Selector.fromIndex(INDEX_SECOND_PERSON))));
        assertParseSuccess(parser, " 3-4", new DeleteCommand(List.of(
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(3)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(4)))));
    }

    @Test
    public void parse_overlappingRanges_returnsDeleteCommand() {
        assertParseSuccess(parser, " 1-3 2-4", new DeleteCommand(List.of(
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(1)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(2)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(3)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(2)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(3)),
                Selector.fromIndex(seedu.address.commons.core.index.Index.fromOneBased(4)))));
    }

    @Test
    public void parse_mixedRangeAndNames_returnsDeleteCommand() {
        Person alice = TypicalPersons.ALICE;
        assertParseSuccess(parser, " 1-2 n:" + alice.getName(), new DeleteCommand(List.of(
                Selector.fromIndex(INDEX_FIRST_PERSON),
                Selector.fromIndex(INDEX_SECOND_PERSON),
                Selector.fromName(alice.getName()))));
    }

    @Test
    public void parse_invalidRanges_throwsParseException() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCommand.MESSAGE_USAGE.replace("n/", "n:"));
        // inverted
        assertParseFailure(parser, " 5-2", expected);
        // zero start
        assertParseFailure(parser, " 0-3", expected);
        // malformed
        assertParseFailure(parser, " 1-", expected);
        assertParseFailure(parser, " -3", expected);
        assertParseFailure(parser, " 1-2-3", expected);
    }

    @Test
    public void parse_validNameArg_returnsDeleteCommand() {
        Person alice = TypicalPersons.ALICE;
        String input = " n:" + alice.getName();

        assertParseSuccess(parser, input, new DeleteCommand(List.of(Selector.fromName(alice.getName()))));
    }

    @Test
    public void parse_multiWordNameArg_returnsDeleteCommand() {
        Person custom = new PersonBuilder().withName("Alice Tan").build();
        String input = " n:" + custom.getName();

        assertParseSuccess(parser, input, new DeleteCommand(List.of(Selector.fromName(custom.getName()))));
    }

    @Test
    public void parse_multipleNameArgs_returnsDeleteCommand() {
        Person alice = TypicalPersons.ALICE;
        Person benson = TypicalPersons.BENSON;
        String input = " n:" + alice.getName() + " n:" + benson.getName();

        assertParseSuccess(parser, input, new DeleteCommand(List.of(
                Selector.fromName(alice.getName()), Selector.fromName(benson.getName()))));
    }

    @Test
    public void parse_mixedSelectors_returnsDeleteCommand() {
        Person alice = TypicalPersons.ALICE;
        String input = " 1 n:" + alice.getName();

        assertParseSuccess(parser, input, new DeleteCommand(List.of(
                Selector.fromIndex(INDEX_FIRST_PERSON), Selector.fromName(alice.getName()))));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCommand.MESSAGE_USAGE.replace("n/", "n:")));
    }

    @Test
    public void parse_emptyName_throwsParseException() {
        assertParseFailure(parser, " n:   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteCommand.MESSAGE_USAGE.replace("n/", "n:")));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCommand.MESSAGE_USAGE.replace("n/", "n:")));
    }
}

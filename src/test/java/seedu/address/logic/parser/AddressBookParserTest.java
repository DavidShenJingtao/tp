package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListSessionCommand;
import seedu.address.logic.commands.SessionsCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.SessionMatchPredicate;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(List.of(DeleteCommand.Selector.fromIndex(INDEX_FIRST_PERSON))), command);
    }

    @Test
    public void parseCommand_deleteAlias_del() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                "del " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(List.of(DeleteCommand.Selector.fromIndex(INDEX_FIRST_PERSON))), command);
    }

    @Test
    public void parseCommand_deleteAlias_rm() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                "rm " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(List.of(DeleteCommand.Selector.fromIndex(INDEX_FIRST_PERSON))), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        assertEquals(new ExportCommand(), parser.parseCommand(ExportCommand.COMMAND_WORD));
        assertEquals(new ExportCommand(Paths.get("reports.csv")),
                parser.parseCommand(ExportCommand.COMMAND_WORD + " reports"));
    }

    @Test
    public void parseCommand_find() throws Exception {
        String keyword = "foo bar baz";
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keyword);
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(Collections.singletonList(keyword))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_undo_success() throws Exception {
        assertTrue(parser.parseCommand("undo") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoWithArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parseCommand("undo something"));
    }

    @Test
    public void parseCommand_listSession() throws Exception {
        Session session = new Session("S1");
        ListSessionCommand command = (ListSessionCommand) parser.parseCommand(
                ListSessionCommand.COMMAND_WORD + " S1");
        assertEquals(new ListSessionCommand(new SessionMatchPredicate(session)), command);
    }

    @Test
    public void parseCommand_sessions() throws Exception {
        assertTrue(parser.parseCommand(SessionsCommand.COMMAND_WORD) instanceof SessionsCommand);
        assertTrue(parser.parseCommand(SessionsCommand.COMMAND_WORD + " 3") instanceof SessionsCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}

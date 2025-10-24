package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

class CommandInputHandlerTest {

    @Test
    void onEnter_success_clears() {
        CommandInputHandler.Executor ok = cmd -> new CommandResult("OK");
        var h = new CommandInputHandler(ok);
        assertEquals(CommandInputHandler.Outcome.CLEAR_INPUT, h.onEnter("help"));
    }

    @Test
    void onEnter_commandError_keeps() {
        CommandInputHandler.Executor bad = cmd -> {
            throw new CommandException("boom");
        };
        var h = new CommandInputHandler(bad);
        assertEquals(CommandInputHandler.Outcome.KEEP_INPUT, h.onEnter("x"));
    }

    @Test
    void onEnter_parseError_keeps() {
        CommandInputHandler.Executor bad = cmd -> {
            throw new ParseException("nope");
        };
        var h = new CommandInputHandler(bad);
        assertEquals(CommandInputHandler.Outcome.KEEP_INPUT, h.onEnter("?"));
    }
}

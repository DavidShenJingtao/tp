package seedu.address.ui;

import java.util.Objects;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Handles the logic of processing a user command input when the Enter key is pressed.
 * <p>
 * This class is responsible for executing the given command text via the provided
 * {@link Executor} and determining whether the command box input should be cleared
 * or kept based on whether execution succeeds or fails.
 * </p>
 * <p>
 * It contains no JavaFX code and can be tested independently of the UI.
 * </p>
 */
public class CommandInputHandler {

    /**
     * Represents a function that can execute a command text and return its result.
     */
    public interface Executor {
        /**
         * Executes the specified command text and returns the corresponding result.
         *
         * @param commandText The full command text entered by the user.
         * @return A {@link CommandResult} representing the outcome of the command.
         * @throws CommandException If an error occurs during command execution.
         * @throws ParseException If the user input could not be parsed correctly.
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    /**
     * Represents the outcome of executing a command.
     * <ul>
     *     <li>{@code CLEAR_INPUT} — command executed successfully, so the UI should clear the text field.</li>
     *     <li>{@code KEEP_INPUT} — command failed to execute or parse, so the UI should keep the current input.</li>
     * </ul>
     */
    public enum Outcome {
        CLEAR_INPUT,
        KEEP_INPUT
    }

    private final Executor executor;

    /**
     * Constructs a {@code CommandInputHandler} with the given {@link Executor}.
     *
     * @param executor The executor used to run command texts. Must not be {@code null}.
     */
    public CommandInputHandler(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    /**
     * Executes the given command text using the provided {@link Executor}.
     * <p>
     * If execution succeeds, the outcome is {@link Outcome#CLEAR_INPUT};
     * if a {@link CommandException} or {@link ParseException} occurs,
     * the outcome is {@link Outcome#KEEP_INPUT}.
     * </p>
     *
     * @param text The command text entered by the user.
     * @return The {@link Outcome} indicating whether the input should be cleared or kept.
     */
    public Outcome onEnter(String text) {
        try {
            executor.execute(text);
            return Outcome.CLEAR_INPUT;
        } catch (CommandException | ParseException e) {
            return Outcome.KEEP_INPUT;
        }
    }
}

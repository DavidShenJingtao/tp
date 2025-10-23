package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.undo.UndoHistory;
import seedu.address.model.Model;

/**
 * Undoes the most recent state-changing command executed in the current session.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the most recent command that modified data.";
    public static final String MESSAGE_SUCCESS = "Undo successful (reverted: %1$s)";
    public static final String MESSAGE_NOTHING_TO_UNDO = "There is no command to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!UndoHistory.canUndo()) {
            throw new CommandException(MESSAGE_NOTHING_TO_UNDO);
        }

        String revertedLabel = UndoHistory.restorePreviousState(model);
        if (revertedLabel == null || revertedLabel.isBlank()) {
            revertedLabel = "unknown";
        }
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, revertedLabel));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof UndoCommand;
    }

    @Override
    public int hashCode() {
        return UndoCommand.class.hashCode();
    }
}

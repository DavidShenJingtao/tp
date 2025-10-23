package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    private String undoLabel;

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Returns {@code true} if this command mutates the underlying address book.
     * Commands should override this when they modify stored data so that undo history can be recorded.
     */
    public boolean isStateChanging() {
        return false;
    }

    /**
     * Sets the label used when reporting that this command has been undone.
     */
    public void setUndoLabel(String undoLabel) {
        this.undoLabel = undoLabel;
    }

    /**
     * Returns the label used when reporting that this command has been undone.
     */
    public String getUndoLabel() {
        return undoLabel;
    }

}

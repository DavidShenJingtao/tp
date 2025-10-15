package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.SessionMatchPredicate;

/**
 * Lists all persons in TAConnect whose session equals any of the argument sessions.
 */
public class ListSessionCommand extends Command {

    public static final String COMMAND_WORD = "listsession";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons whose session equals any of"
            + "the specified sessions (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: SESSION [MORE_SESSIONS]...\n"
            + "Example: " + COMMAND_WORD + " S1 G30 H5";

    private final SessionMatchPredicate predicate;

    public ListSessionCommand(SessionMatchPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListSessionCommand)) {
            return false;
        }

        ListSessionCommand otherListSessionCommand = (ListSessionCommand) other;
        return predicate.equals(otherListSessionCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

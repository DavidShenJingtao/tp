package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Undoes the most recent delete operation in the current session.
 */
public class UndoDeleteCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the most recent delete operation in this session.\n"
            + "Examples:\n"
            + COMMAND_WORD + " delete\n"
            + COMMAND_WORD + " del\n"
            + COMMAND_WORD + " rm";

    public static final String MESSAGE_NOTHING_TO_UNDO = "There is no delete operation to undo";
    public static final String MESSAGE_UNDO_DELETE_PERSON_SUCCESS = "Restored Person: %1$s";
    public static final String MESSAGE_UNDO_DELETE_MULTIPLE_PERSON_SUCCESS = "Restored Persons:\n%1$s";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> toRestore = DeleteUndoBuffer.popLatest();
        if (toRestore == null || toRestore.isEmpty()) {
            throw new CommandException(MESSAGE_NOTHING_TO_UNDO);
        }

        List<Person> restored = new ArrayList<>();
        for (Person p : toRestore) {
            if (!model.hasPerson(p)) {
                model.addPerson(p);
                restored.add(p);
            }
        }

        if (restored.isEmpty()) {
            // Nothing restored because duplicates exist now; still consider it done.
            return new CommandResult("No contacts restored (duplicates already exist)");
        }

        if (restored.size() == 1) {
            String single = Messages.format(restored.get(0));
            return new CommandResult(String.format(MESSAGE_UNDO_DELETE_PERSON_SUCCESS, single));
        }

        String msg = restored.stream().map(Messages::format).collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_UNDO_DELETE_MULTIPLE_PERSON_SUCCESS, msg));
    }
}

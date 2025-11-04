package seedu.address.logic.commands;

import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Session;

/**
 * List all sessions that exist in TAConnect.
 */
public class SessionsCommand extends Command {

    public static final String COMMAND_WORD = "sessions";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all sessions that exist.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_LIST_ALL_SESSIONS = "%1$d sessions found in TAConnect. Here is the list: %2$s";

    public static final String MESSAGE_NO_SESSIONS_FOUND = "No sessions found in TAConnect.";

    @Override
    public CommandResult execute(Model model) {
        Set<Session> sessions = model.getAddressBook().getCounter().listAllSessions();
        if (sessions.size() <= 0) {
            return new CommandResult(MESSAGE_NO_SESSIONS_FOUND);
        } else {
            // Sort sessions in alphanumeric order.
            return new CommandResult(String.format(
                MESSAGE_LIST_ALL_SESSIONS, sessions.size(),
                sessions.stream().map(Session::toString)
                        .sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(", "))
            ));
        }
    }
}

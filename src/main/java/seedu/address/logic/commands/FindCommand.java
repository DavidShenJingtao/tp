package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains the given keyword.
 * Keyword matching is case insensitive and performed on substrings.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain the specified "
            + "keyword (case-insensitive substring match) and displays them as a list.\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " alice";

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> filteredPersons = model.getFilteredPersonList();
        if (filteredPersons.isEmpty()) {
            return new CommandResult(String.format(Messages.MESSAGE_FIND_NO_MATCH, getDisplayKeyword()));
        }

        StringBuilder feedback = new StringBuilder();
        int matchCount = filteredPersons.size();
        feedback.append(String.format(Messages.MESSAGE_FIND_PERSONS_FOUND_OVERVIEW,
                matchCount, matchCount == 1 ? "" : "s"));

        for (Person person : filteredPersons) {
            feedback.append(System.lineSeparator())
                    .append(Messages.format(person));
        }

        return new CommandResult(feedback.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }

    private String getDisplayKeyword() {
        return predicate.getKeywords().stream()
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .collect(Collectors.joining(" "));
    }
}

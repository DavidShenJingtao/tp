package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes one or more persons identified by displayed index or exact name.\n"
            + "Parameters: INDEX [MORE_INDEXES] (positive integers) [n/NAME] [n/MORE_NAMES]\n"
            + "Examples:\n"
            + COMMAND_WORD + " 1\n"
            + COMMAND_WORD + " 1 3\n"
            + COMMAND_WORD + " n/Alice Tan";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_MULTIPLE_PERSON_SUCCESS = "Deleted Persons:\n%1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND_BY_NAME = "No person with the name: %1$s";

    private final List<Selector> selectors;

    /**
     * Creates a {@code DeleteCommand} targeting the given selectors.
     *
     * @param selectors selectors describing persons to remove.
     * @throws IllegalArgumentException if {@code selectors} is empty.
     */
    public DeleteCommand(List<Selector> selectors) {
        requireNonNull(selectors);
        if (selectors.isEmpty()) {
            throw new IllegalArgumentException("selectors cannot be empty");
        }
        this.selectors = List.copyOf(selectors);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        List<Person> personsToDelete = new ArrayList<>();
        for (Selector selector : selectors) {
            if (selector.isIndex()) {
                personsToDelete.add(mapIndexToPerson(selector.getIndex(), lastShownList));
                continue;
            }

            personsToDelete.addAll(mapNameToPersons(selector.getName(), lastShownList));
        }

        Set<Person> uniquePersonsToDelete = new LinkedHashSet<>(personsToDelete);
        uniquePersonsToDelete.forEach(model::deletePerson);
        // record for undo
        DeleteUndoBuffer.pushBatch(new ArrayList<>(uniquePersonsToDelete));
        return new CommandResult(buildSuccessMessage(uniquePersonsToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return selectors.equals(otherDeleteCommand.selectors);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("selectors", selectors)
                .toString();
    }

    private Person mapIndexToPerson(Index targetIndex, List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(targetIndex.getZeroBased());
    }

    private Collection<Person> mapNameToPersons(Name name, List<Person> lastShownList) throws CommandException {
        List<Person> matchedPersons = lastShownList.stream()
                .filter(person -> person.getName().equals(name))
                .collect(Collectors.toList());

        if (matchedPersons.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND_BY_NAME, name));
        }

        return matchedPersons;
    }

    private String buildSuccessMessage(Collection<Person> personsToDelete) {
        if (personsToDelete.size() == 1) {
            Person person = personsToDelete.iterator().next();
            return String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(person));
        }

        String deletedPersons = personsToDelete.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));

        return String.format(MESSAGE_DELETE_MULTIPLE_PERSON_SUCCESS, deletedPersons);
    }

    /**
     * Represents a selector used to identify persons targeted by {@code DeleteCommand}.
     */
    public static class Selector {
        private final Index index;
        private final Name name;

        private Selector(Index index, Name name) {
            this.index = index;
            this.name = name;
        }

        /**
         * Creates an index selector.
         */
        public static Selector fromIndex(Index index) {
            requireNonNull(index);
            return new Selector(index, null);
        }

        /**
         * Creates a case-sensitive name selector.
         */
        public static Selector fromName(Name name) {
            requireNonNull(name);
            return new Selector(null, name);
        }

        public boolean isIndex() {
            return index != null;
        }

        public Index getIndex() {
            return index;
        }

        public Name getName() {
            return name;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof Selector)) {
                return false;
            }

            Selector otherSelector = (Selector) other;
            return Objects.equals(index, otherSelector.index)
                    && Objects.equals(name, otherSelector.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, name);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("index", index)
                    .add("name", name)
                    .toString();
        }
    }
}

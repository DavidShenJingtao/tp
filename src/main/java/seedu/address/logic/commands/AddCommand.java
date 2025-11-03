package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.model.person.Person.MESSAGE_INSTRUCTOR_STAFF;
import static seedu.address.model.person.Person.MESSAGE_STUDENT_TA;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyPersonAndSessionCounter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.Type;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the contact list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_TYPE + "TYPE "
            + PREFIX_TELEGRAM + "TELEGRAM "
            + PREFIX_SESSION + "SESSION "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_TYPE + "student "
            + PREFIX_TELEGRAM + "@ABC12 "
            + PREFIX_SESSION + "G1";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the contact list, "
            + "or the added email cannot belong to multiple people";

    public static final short MAX_PERSON_COUNT = 2500;
    public static final short MAX_SESSION_COUNT = 250;

    public static final String MESSAGE_MAX_PERSON_COUNT_REACHED = "The contact list has reached the maximum "
                                                                      + MAX_PERSON_COUNT + " person limit";
    public static final String MESSAGE_MAX_SESSION_COUNT_REACHED = "The contact list has reached the maximum "
                                                                       + MAX_SESSION_COUNT + " session limit";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    /**
     * Check model's current capacity and throw an exception if adding a person
     * would exceed capacity.
     */
    public void checkCapacity(Model model, Person person) throws CommandException {
        ReadOnlyPersonAndSessionCounter counter = model.getAddressBook().getCounter();

        if (counter.getPersonCountIfPersonAdded() > MAX_PERSON_COUNT) {
            throw new CommandException(MESSAGE_MAX_PERSON_COUNT_REACHED);
        }

        Optional<Session> s = person.getSession();
        if (s.isPresent() && counter.getUniqueSessionCountIfSessionAdded(s.get()) > MAX_SESSION_COUNT) {
            throw new CommandException(MESSAGE_MAX_SESSION_COUNT_REACHED);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        checkCapacity(model, toAdd);

        Type type = toAdd.getType();
        boolean hasSession = toAdd.getSession().isPresent();

        if ((type.isStudent() || type.isTa()) && !hasSession) {
            throw new CommandException(MESSAGE_STUDENT_TA);
        }

        if ((type.isInstructor() || type.isStaff()) && hasSession) {
            throw new CommandException(MESSAGE_INSTRUCTOR_STAFF);
        }

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean isStateChanging() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}

package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.PersonAndSessionCounter;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;


/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_MAX_PERSON_COUNT_REACHED = "Persons list contains more than maximum "
                                                                      + AddCommand.MAX_PERSON_COUNT + " person limit.";
    public static final String MESSAGE_MAX_SESSION_COUNT_REACHED =
                                   "Persons list contains more than maximum "
                                   + AddCommand.MAX_SESSION_COUNT + " session limit.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            PersonAndSessionCounter counter = addressBook.getCounter();
            if (counter.getPersonCountIfPersonAdded() > AddCommand.MAX_PERSON_COUNT) {
                throw new IllegalValueException(MESSAGE_MAX_PERSON_COUNT_REACHED);
            }
            Optional<Session> s = person.getSession();
            if (s.isPresent() && counter.getUniqueSessionCountIfSessionAdded(s.get()) > AddCommand.MAX_SESSION_COUNT) {
                throw new IllegalValueException(MESSAGE_MAX_SESSION_COUNT_REACHED);
            }
            addressBook.addPerson(person);
        }
        return addressBook;
    }

}

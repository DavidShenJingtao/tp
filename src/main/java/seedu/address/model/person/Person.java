package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final String MESSAGE_INSTRUCTOR_STAFF = "Instructors and staff should not have a session.";
    public static final String MESSAGE_STUDENT_TA = "Students and TAs must have a session assigned.";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Optional<TelegramUsername> telegramUsername; // optional
    private final Type type;
    private final Optional<Session> session; // present only if student or ta

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Type type,
                  TelegramUsername telegramUsername, Session session) {
        requireAllNonNull(name, phone, email, type);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegramUsername = Optional.ofNullable(telegramUsername);
        this.type = type;
        this.session = Optional.ofNullable(session);
    }

    /**
     * Overloaded constructor for students or ta without telegramUsername.
     */
    public Person(Name name, Phone phone, Email email, Type type, Session session) {
        this(name, phone, email, type, null, session);
    }

    /**
     * Overloaded constructor for instructors or staff with telegramUsername.
     */
    public Person(Name name, Phone phone, Email email, Type type, TelegramUsername telegramUsername) {
        this(name, phone, email, type, telegramUsername, null);
    }

    /**
     * Overloaded constructor for instructors or staff without telegramUsername.
     */
    public Person(Name name, Phone phone, Email email, Type type) {
        this(name, phone, email, type, null, null);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Optional<TelegramUsername> getTelegramUsername() {
        return telegramUsername;
    }

    public Optional<Session> getSession() {
        return session;
    }

    public Type getType() {
        return type;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && telegramUsername.equals(otherPerson.telegramUsername)
                && session.equals(otherPerson.session)
                && type.equals(otherPerson.type);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, telegramUsername, type, session);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("type", type);
        telegramUsername.ifPresent(t -> builder.add("telegramUsername", t));
        session.ifPresent(s -> builder.add("session", s));
        return builder.toString();
    }

}

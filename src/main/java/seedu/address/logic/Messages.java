package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_EMPTY_FIND_KEYWORD = "Keyword to find cannot be empty.";
    public static final String MESSAGE_INVALID_FIND_KEYWORD =
            "Keyword to find is invalid! Use only letters (a-z, A-Z), spaces, apostrophes (' ’ ʼ), hyphens (-),"
            + " periods (.), or slashes (/).";
    public static final String MESSAGE_FIND_NO_MATCH = "Oops, no one's name contains %1$s";
    public static final String MESSAGE_FIND_PERSONS_FOUND_OVERVIEW = "%1$d person%2$s found";
    public static final String MESSAGE_INVALID_SESSION_FORMAT = "Specified session has an invalid format. "
            + "It should have 1–2 uppercase letters, followed by a number from 1–99 "
            + "(leading zero allowed for 1–9), and may end with an uppercase letter (e.g., G1, AB9, F01, T07B, AB99C).";
    public static final String MESSAGE_SESSION_NOT_FOUND = "Specified session %1$s does not exist.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Type: ")
                .append(person.getType())
                .append("; Name: ")
                .append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail());
        person.getTelegramUsername()
                .ifPresent(telegram -> builder.append("; Telegram: ").append(telegram));
        person.getSession()
                .ifPresent(session -> builder.append("; Session: ").append(session));

        return builder.toString();
    }

}

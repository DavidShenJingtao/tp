package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}.
 *
 * Enforcement (pragmatic, user-friendly subset of RFC rules):
 * - Structure: exactly one '@', no spaces.
 * - Lengths: total ≤ 254 chars; local-part ≤ 64; each domain label ≤ 63.
 * - Local-part: letters/digits with separators [., _, +, -]; cannot start/end with a separator; no consecutive dots.
 * - Domain: labels separated by dots; each starts/ends with alphanumeric; hyphens allowed inside; TLD ≥ 2 chars.
 * - Case: domain is case-insensitive and normalized to lowercase on store; local-part preserved as entered.
 */
public class Email {

    public static final String MESSAGE_CONSTRAINTS =
            "Emails must be of the form local-part@domain and adhere to:\n"
            + "1) exactly one '@', no spaces; 2) total length ≤ 254;\n"
            + "3) local-part ≤ 64 using [A-Za-z0-9] with [._+-] as separators "
            + "(no leading/trailing separator, no consecutive dots);\n"
            + "4) domain contains at least one '.', labels start/end alphanumeric, hyphens allowed inside, "
            + "final label (TLD) ≥ 2 chars;\n"
            + "5) domain is case-insensitive (stored lowercased).";

    // Regex parts per enforcement above
    private static final String LOCAL = "[A-Za-z0-9]+([._+-][A-Za-z0-9]+)*";
    private static final String DOMAIN_LABEL = "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?";
    private static final String DOMAIN = "(?:" + DOMAIN_LABEL + "\\.)+[A-Za-z0-9]{2,}"; // at least one dot, TLD ≥ 2
    public static final String VALIDATION_REGEX = "^" + LOCAL + "@" + DOMAIN + "$";

    private static final int MAX_TOTAL_LENGTH = 254;
    private static final int MAX_LOCAL_LENGTH = 64;
    private static final int MAX_LABEL_LENGTH = 63;

    // For normalization
    private static final java.util.regex.Pattern SPLIT_AT_AT =
            java.util.regex.Pattern.compile("@", java.util.regex.Pattern.LITERAL);

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        // Normalize: lower-case the domain part; preserve local-part as entered
        String[] parts = SPLIT_AT_AT.split(email, -1);
        String local = parts[0];
        String domainLower = parts[1].toLowerCase(java.util.Locale.ROOT);
        value = local + "@" + domainLower;
    }

    /**
     * Returns true if a given string is a valid email according to the enforcement rules.
     * Applies structural and length checks in addition to the regex.
     */
    public static boolean isValidEmail(String test) {
        requireNonNull(test);
        if (test.length() > MAX_TOTAL_LENGTH) {
            return false;
        }
        int atIdx = test.indexOf('@');
        if (atIdx <= 0) {
            return false; // missing or at start
        }
        // exactly one '@'
        if (test.indexOf('@', atIdx + 1) != -1) {
            return false;
        }
        String local = test.substring(0, atIdx);
        String domain = test.substring(atIdx + 1);
        if (local.length() == 0 || local.length() > MAX_LOCAL_LENGTH) {
            return false;
        }
        // check domain label lengths ≤ 63
        String[] labels = domain.split("\\.");
        for (String label : labels) {
            if (label.isEmpty() || label.length() > MAX_LABEL_LENGTH) {
                return false;
            }
        }
        // final regex check
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

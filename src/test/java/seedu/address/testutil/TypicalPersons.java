package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SESSION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SESSION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_TA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder()
            .withName("Alice Pauline")
            .withPhone("94351253")
            .withEmail("alice@example.com")
            .withType("student")
            .withTelegram("@alicep")
            .withSession("S1")
            .build();

    public static final Person BENSON = new PersonBuilder()
            .withName("Benson Meier")
            .withPhone("98765432")
            .withEmail("benson@example.com")
            .withType("ta")
            .withTelegram(null)
            .withSession("S2")
            .build();

    public static final Person CARL = new PersonBuilder()
            .withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("carl@example.com")
            .withType("instructor")
            .withTelegram(null)
            .build();

    public static final Person DANIEL = new PersonBuilder()
            .withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("daniel@example.com")
            .withType("staff")
            .withTelegram(null)
            .build();

    public static final Person ELLE = new PersonBuilder()
            .withName("Elle Meyer")
            .withPhone("94822242")
            .withEmail("elle@example.com")
            .withType("student")
            .withTelegram(null)
            .withSession("S3")
            .build();

    public static final Person FIONA = new PersonBuilder()
            .withName("Fiona Kunz")
            .withPhone("94824270")
            .withEmail("fiona@example.com")
            .withType("instructor")
            .withTelegram("@fionak")
            .build();

    public static final Person GEORGE = new PersonBuilder()
            .withName("George Best")
            .withPhone("94824420")
            .withEmail("george@example.com")
            .withType("staff")
            .withTelegram("@georgeBest")
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder()
            .withName("Hoon Meier")
            .withPhone("84824240")
            .withEmail("stefan@example.com")
            .withType("ta")
            .withTelegram("@HoonMei")
            .withSession("S1")
            .build();

    public static final Person IDA = new PersonBuilder()
            .withName("Ida Mueller")
            .withPhone("84821310")
            .withEmail("hans@example.com")
            .withType("instructor")
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder()
            .withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withType(VALID_TYPE_STUDENT)
            .withTelegram(VALID_TELEGRAM_AMY)
            .withSession(VALID_SESSION_AMY)
            .build();

    public static final Person BOB = new PersonBuilder()
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withType(VALID_TYPE_TA)
            .withTelegram(VALID_TELEGRAM_BOB)
            .withSession(VALID_SESSION_BOB)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER
    private TypicalPersons() {} // prevents instantiation

    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}

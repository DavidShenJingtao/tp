package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Session;
import seedu.address.model.person.TelegramUsername;
import seedu.address.model.person.Type;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TELEGRAM = "@";
    private static final String INVALID_TYPE = "alien";
    private static final String INVALID_SESSION = "";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TELEGRAM = "@rachel";
    private static final String VALID_TYPE = "student";
    private static final String VALID_SESSION = "L1";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseType_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseType((String) null));
    }

    @Test
    public void parseType_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseType(INVALID_TYPE));
    }

    @Test
    public void parseType_validValueWithoutWhitespace_returnsType() throws Exception {
        Type expectedAddress = new Type(VALID_TYPE);
        assertEquals(expectedAddress, ParserUtil.parseType(VALID_TYPE));
    }

    @Test
    public void parseType_validValueWithWhitespace_returnsTrimmedType() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_TYPE + WHITESPACE;
        Type expectedAddress = new Type(VALID_TYPE);
        assertEquals(expectedAddress, ParserUtil.parseType(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTelegram_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTelegramUsername(null));
    }

    @Test
    public void parseTelegram_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTelegramUsername(INVALID_TELEGRAM));
    }

    @Test
    public void parseTelegram_validValueWithoutWhitespace_returnsTelegram() throws Exception {
        TelegramUsername expectedTelegram = new TelegramUsername(VALID_TELEGRAM);
        assertEquals(expectedTelegram, ParserUtil.parseTelegramUsername(VALID_TELEGRAM));
    }

    @Test
    public void parseTelegram_validValueWithWhitespace_returnsTrimmedTelegram() throws Exception {
        String telegramWithWhitespace = WHITESPACE + VALID_TELEGRAM + WHITESPACE;
        TelegramUsername expectedTelegram = new TelegramUsername(VALID_TELEGRAM);
        assertEquals(expectedTelegram, ParserUtil.parseTelegramUsername(telegramWithWhitespace));
    }

    @Test
    public void parseSession_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSession((String) null));
    }

    @Test
    public void parseSession_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSession(INVALID_SESSION));
    }

    @Test
    public void parseSession_validValueWithoutWhitespace_returnsSession() throws Exception {
        Session expected = new Session(VALID_SESSION);
        assertEquals(expected, ParserUtil.parseSession(VALID_SESSION));
    }

    @Test
    public void parseSession_validValueWithWhitespace_returnsTrimmedSession() throws Exception {
        String input = WHITESPACE + VALID_SESSION + WHITESPACE;
        Session expected = new Session(VALID_SESSION);
        assertEquals(expected, ParserUtil.parseSession(input));
    }
}

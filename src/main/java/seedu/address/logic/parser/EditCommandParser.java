package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.Type;


/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_TYPE, PREFIX_TELEGRAM, PREFIX_SESSION);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TYPE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_TYPE).isPresent()) {
            editPersonDescriptor.setType(ParserUtil.parseType(argMultimap.getValue(PREFIX_TYPE).get()));
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            String telegramInput = argMultimap.getValue(PREFIX_TELEGRAM).get();

            if (telegramInput.isEmpty()) {
                editPersonDescriptor.setTelegramUsername(Optional.empty());
            } else {
                editPersonDescriptor.setTelegramUsername(Optional.of(ParserUtil.parseTelegramUsername(telegramInput)));
            }
        }
        if (argMultimap.getValue(PREFIX_SESSION).isPresent()) {
            String session = argMultimap.getValue(PREFIX_SESSION).get();
            editPersonDescriptor.setSession(Optional.of(ParserUtil.parseSession(session)));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
        if ((editPersonDescriptor.getType() != null && editPersonDescriptor.getType().isPresent())
                || (editPersonDescriptor.getSession() != null && editPersonDescriptor.getSession().isPresent())) {
            validateTypeSession(editPersonDescriptor);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Checks whether type and session fields satisfy the constraint.
     * @throws ParseException if the constraint is violated.
     */
    public void validateTypeSession(EditPersonDescriptor editPersonDescriptor) throws ParseException {
        if (editPersonDescriptor.getType().isPresent()) {
            Type editedType = editPersonDescriptor.getType().get();
            Optional<Session> editedSession = editPersonDescriptor.getSession();

            if ((editedType.isStudent() || editedType.isTa()) && editedSession.isEmpty()) {
                throw new ParseException(Person.MESSAGE_STUDENT_TA);
            }

            if ((editedType.isInstructor() || editedType.isStaff()) && editedSession.isPresent()) {
                throw new ParseException(Person.MESSAGE_INSTRUCTOR_STAFF);
            }
        }
    }

}

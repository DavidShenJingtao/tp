package seedu.address.testutil;

import java.util.Optional;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Session;
import seedu.address.model.person.TelegramUsername;
import seedu.address.model.person.Type;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setType(person.getType());
        descriptor.setTelegramUsername(person.getTelegramUsername());
        descriptor.setSession(person.getSession());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Type} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withType(String type) {
        descriptor.setType(new Type(type));
        return this;
    }

    /**
     * Sets the {@code TelegramUsername} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withTelegram(String telegramUsername) {
        if (telegramUsername == null) {
            descriptor.setTelegramUsername(Optional.empty());
        } else {
            descriptor.setTelegramUsername(Optional.of(new TelegramUsername(telegramUsername)));
        }
        return this;
    }

    /**
     * Sets the {@code Session} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withSession(String session) {
        if (session == null) {
            descriptor.setSession(Optional.empty());
        } else {
            descriptor.setSession(Optional.of(new Session(session)));
        }
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}

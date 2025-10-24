// codecov:ignore-file
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component responsible for receiving user command inputs.
 * UI stays thin and delegates logic to testable helpers:
 * - CommandInputHandler (what to do on Enter)
 * - HistoryNavigator (↑/↓ history and buffer)
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final CommandInputHandler inputHandler;
    private final HistoryNavigator history = new HistoryNavigator();

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.inputHandler = new CommandInputHandler(commandExecutor::execute);

        // Reset style whenever text changes, and update current buffer for history
        commandTextField.textProperty().addListener((obs, oldV, newV) -> {
            setStyleToDefault();
            history.setBuffer(newV);
        });

        // Listen for ↑ / ↓ key presses to navigate history
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                handleHistoryUp();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                handleHistoryDown();
                event.consume();
            }
        });
    }

    /** Handles Enter press from FXML (onAction of the TextField). */
    @FXML
    private void handleCommandEntered() {
        final String commandText = commandTextField.getText();
        if (commandText == null || commandText.isEmpty()) {
            return;
        }

        CommandInputHandler.Outcome outcome = inputHandler.onEnter(commandText);
        switch (outcome) {
        case CLEAR_INPUT:
            history.add(commandText); // record successful command
            commandTextField.clear();
            break;
        case KEEP_INPUT:
            history.add(commandText); // optionally record failed ones too
            setStyleToIndicateCommandFailure();
            break;
        default:
            // no-op
        }
    }

    /** Sets the command box style back to default. */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a failed command. */
    private void setStyleToIndicateCommandFailure() {
        var styleClass = commandTextField.getStyleClass();
        if (!styleClass.contains(ERROR_STYLE_CLASS)) {
            styleClass.add(ERROR_STYLE_CLASS);
        }
    }

    /** Navigate up the history; places text and moves caret to end. */
    private void handleHistoryUp() {
        String prev = history.up();
        commandTextField.setText(prev);
        commandTextField.positionCaret(prev.length());
    }

    /** Navigate down the history or restore buffer; places text and moves caret to end. */
    private void handleHistoryDown() {
        String next = history.down();
        commandTextField.setText(next);
        commandTextField.positionCaret(next.length());
    }

    // ==== Public types unchanged (keeps compatibility with the rest of the app) ====

    /** Represents a function that can execute commands. */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}

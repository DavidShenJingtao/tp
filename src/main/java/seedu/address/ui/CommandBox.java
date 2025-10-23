package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final CommandHistory history = new CommandHistory();
    private HistorySnapshot snapshot = history.snapshot("");

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
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

        // Keep snapshot buffer updated when typing
        commandTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (snapshot != null) {
                snapshot.setCurrentBuffer(newV);
            }
        });

    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            history.add(commandText); // record successful command
            snapshot = history.snapshot("");
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            history.add(commandText); // optionally record failed command too
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Handles the UP arrow key press event.
     * Displays the previous command from the command history in the command box,
     * if available.
     */
    private void handleHistoryUp() {
        if (snapshot == null) {
            return;
        }
        String prev = snapshot.previous();
        commandTextField.setText(prev);
        commandTextField.positionCaret(prev.length());
    }

    /**
     * Handles the DOWN arrow key press event.
     * Displays the next command from the command history, or restores the
     * current unfinished input if the user has reached the end of the history.
     */
    private void handleHistoryDown() {
        if (snapshot == null) {
            return;
        }
        String next = snapshot.next();
        commandTextField.setText(next);
        commandTextField.positionCaret(next.length());
    }


    /**
     * Represents a function that can execute commands.
     */
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

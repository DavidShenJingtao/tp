package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Lightweight tests for {@link CommandBox} without TestFX.
 * - Boots JavaFX toolkit once.
 * - Uses reflection to invoke private @FXML methods.
 * - Runs UI-affecting calls on the JavaFX Application Thread.
 */
public class CommandBoxTest {

    private CommandBox commandBox;
    private MockExecutor mockExecutor;

    // ---------- JavaFX setup ----------

    @BeforeAll
    static void initJavaFx() {
        // Skip tests in headless CI environments
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Skipping JavaFX tests in headless environment");
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // FX already started
        }
    }

    @BeforeEach
    public void setUp() {
        mockExecutor = new MockExecutor();
        runFxAndWait(() -> commandBox = new CommandBox(mockExecutor));
    }

    // ---------- Tests ----------

    @Test
    public void enterSuccessAddsToHistoryAndClearsField() {
        TextField field = getTextField();
        runFxAndWait(() -> field.setText("list"));
        invokeHandleCommandEntered();
        assertEquals("list", mockExecutor.getLastExecuted());
        assertEquals("", getTextFieldText()); // cleared after success
    }

    @Test
    public void enterFailureSetsErrorStyleAndKeepsField() {
        TextField field = getTextField();
        runFxAndWait(() -> field.setText("fail")); // mock throws on "fail"
        invokeHandleCommandEntered();
        assertEquals("fail", mockExecutor.getLastExecuted());
        assertEquals("fail", getTextFieldText()); // not cleared on failure
        assertTrue(getTextField().getStyleClass().contains(getErrorStyleClass()));
    }

    @Test
    public void historyUpDownNavigatesCorrectly() {
        TextField field = getTextField();

        // Execute two commands to populate history
        runFxAndWait(() -> field.setText("list"));
        invokeHandleCommandEntered();
        runFxAndWait(() -> field.setText("find alex"));
        invokeHandleCommandEntered();

        // First UP should show last command
        invokeHandleHistoryUp();
        assertEquals("find alex", getTextFieldText());

        // Second UP should show previous command
        invokeHandleHistoryUp();
        assertEquals("list", getTextFieldText());

        // One DOWN should go back to last command
        invokeHandleHistoryDown();
        assertEquals("find alex", getTextFieldText());

    }


    // ---------- Reflection helpers (wrap exceptions) ----------

    private void invokeHandleCommandEntered() {
        Method m = getDeclaredMethodUnchecked(CommandBox.class, "handleCommandEntered");
        runFxAndWait(() -> invokeUnchecked(m, commandBox));
    }

    private void invokeHandleHistoryUp() {
        Method m = getDeclaredMethodUnchecked(CommandBox.class, "handleHistoryUp");
        runFxAndWait(() -> invokeUnchecked(m, commandBox));
    }

    private void invokeHandleHistoryDown() {
        Method m = getDeclaredMethodUnchecked(CommandBox.class, "handleHistoryDown");
        runFxAndWait(() -> invokeUnchecked(m, commandBox));
    }

    private TextField getTextField() {
        try {
            var f = CommandBox.class.getDeclaredField("commandTextField");
            f.setAccessible(true);
            return (TextField) f.get(commandBox);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private String getTextFieldText() {
        final var ref = new String[1];
        runFxAndWait(() -> ref[0] = getTextField().getText());
        return ref[0];
    }

    private String getErrorStyleClass() {
        try {
            var f = CommandBox.class.getDeclaredField("ERROR_STYLE_CLASS");
            f.setAccessible(true);
            return (String) f.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static Method getDeclaredMethodUnchecked(Class<?> cls, String name) {
        try {
            Method m = cls.getDeclaredMethod(name);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static void invokeUnchecked(Method m, Object target) {
        try {
            m.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- FX thread utility ----------

    private static void runFxAndWait(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
            return;
        }
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                latch.countDown();
            }
        });
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new AssertionError("Timed out waiting for FX task");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
    }

    // ---------- Mock executor ----------

    /** Mock CommandExecutor: succeeds unless command is "fail". */
    private static class MockExecutor implements CommandBox.CommandExecutor {
        private String lastExecuted;

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            lastExecuted = commandText;
            if ("fail".equals(commandText)) {
                throw new CommandException("Simulated failure");
            }
            return new CommandResult("OK");
        }

        String getLastExecuted() {
            return lastExecuted;
        }
    }
}

package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * Minimal headless tests for CommandBox that work reliably on CI.
 * Uses direct JavaFX toolkit bootstrap and reflection to avoid Stage/Scene.
 */
public class CommandBoxTest {

    private static volatile boolean fxStarted = false;
    private CommandBox commandBox;

    // Simple recording executor state
    private final List<String> received = new ArrayList<>();
    private boolean failWithCommandException;
    private boolean failWithParseException;

    private static synchronized void ensureFxToolkitStarted() {
        if (fxStarted) {
            return;
        }
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException alreadyStarted) {
            // Toolkit already started by another test class.
        }
        fxStarted = true;
    }

    @BeforeEach
    void setUp() {
        ensureFxToolkitStarted();

        failWithCommandException = false;
        failWithParseException = false;
        received.clear();

        runFxAndWait(() -> {
            CommandBox.CommandExecutor executor = (String commandText) -> {
                received.add(commandText);
                if (failWithCommandException) {
                    throw new seedu.address.logic.commands.exceptions.CommandException("boom");
                }
                if (failWithParseException) {
                    throw new seedu.address.logic.parser.exceptions.ParseException("nope");
                }
                return new seedu.address.logic.commands.CommandResult("OK");
            };
            commandBox = new CommandBox(executor);
        });

        Region root = commandBox.getRoot();
        assertNotNull(root);
    }

    @Test
    void execute_success_recordsAndClears() {
        TextField tf = getTextField();
        runFxAndWait(() -> tf.setText("help"));
        invokeHandleCommandEntered();

        assertEquals(1, received.size());
        assertEquals("help", received.get(0));
        assertEquals("", getTextSafely(tf));
    }

    @Test
    void execute_failure_recordsAndKeepsText() {
        failWithCommandException = true;

        TextField tf = getTextField();
        runFxAndWait(() -> tf.setText("badcmd"));
        invokeHandleCommandEntered();

        assertEquals(1, received.size());
        assertEquals("badcmd", received.get(0));
        assertEquals("badcmd", getTextSafely(tf));
    }

    // -------- Reflection helpers --------

    private TextField getTextField() {
        return runOnFxAndGet(() -> {
            try {
                Field f = CommandBox.class.getDeclaredField("commandTextField");
                f.setAccessible(true);
                return (TextField) f.get(commandBox);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
    }

    private void invokeHandleCommandEntered() {
        runFxAndWait(() -> {
            try {
                Method m = CommandBox.class.getDeclaredMethod("handleCommandEntered");
                m.setAccessible(true);
                m.invoke(commandBox);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
    }

    private String getTextSafely(TextField tf) {
        return runOnFxAndGet(tf::getText);
    }

    // -------- FX thread utilities --------

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

    private static <T> T runOnFxAndGet(Callable<T> c) {
        if (Platform.isFxApplicationThread()) {
            try {
                return c.call();
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
        final Object[] box = new Object[1];
        runFxAndWait(() -> {
            try {
                box[0] = c.call();
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        });
        @SuppressWarnings("unchecked")
        T t = (T) box[0];
        return t;
    }
}

package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * Minimal, headless-friendly tests for CommandBox that avoid Stage/Scene.
 * We start the JavaFX toolkit once and interact with the private TextField
 * and handler via reflection. Designed to pass on CI with Monocle.
 */
public class CommandBoxTest {

    private CommandBox commandBox;

    // Simple recording executor flags + state
    private final List<String> received = new ArrayList<>();
    private boolean failWithCommandException;
    private boolean failWithParseException;

    // ---------- JavaFX toolkit bootstrap (no Stage/Scene) ----------

    @BeforeAll
    static void initFx() throws Exception {
        // Start JavaFX toolkit once; Monocle headless settings are supplied in Gradle
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            // toolkit already up
            latch.countDown();
        }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Timed out starting JavaFX platform");
        }
    }

    @BeforeEach
    void setUp() {
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

        // sanity
        Region root = commandBox.getRoot();
        assertNotNull(root);
    }

    // ---------- Tests ----------

    @Test
    void execute_success_recordsAndClears() {
        TextField tf = getTextField();
        runFxAndWait(() -> tf.setText("help"));
        invokeHandleCommandEntered();

        assertEquals(1, received.size());
        assertEquals("help", received.get(0));

        // On success the input is cleared in AB3-style CommandBox
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

        // On failure most implementations keep the input for correction
        assertEquals("badcmd", getTextSafely(tf));
    }

    // ---------- Reflection helpers ----------

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

    // ---------- FX thread utilities ----------

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

    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> c) {
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

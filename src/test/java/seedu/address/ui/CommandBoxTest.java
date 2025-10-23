package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import seedu.address.logic.commands.CommandResult;

/**
 * Minimal smoke test for {@code CommandBox}.
 * Initializes JavaFX toolkit headlessly (Monocle) once for all tests.
 */
@Tag("ui")
public class CommandBoxTest {

    @BeforeAll
    static void initFxHeadless() throws InterruptedException {
        // Headless JavaFX (Monocle)
        System.setProperty("java.awt.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("monocle.pixelScaleFactor", "1.0");

        // Start FX toolkit if not already started
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        } catch (IllegalStateException alreadyStarted) {
            // OK: toolkit already running (e.g., when running test suite together)
        }
    }

    @Test
    public void createCommandBox_success() {
        // Dummy executor that always succeeds
        CommandBox.CommandExecutor executor = commandText -> new CommandResult("OK");

        // Construct CommandBox (loads FXML but does not show any window)
        CommandBox commandBox = new CommandBox(executor);

        // Basic sanity checks
        assertNotNull(commandBox);
        assertNotNull(commandBox.getRoot());
    }
}

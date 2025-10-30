package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Tests for {@link StatusBarFooter}.
 */
public class StatusBarFooterTest {

    @BeforeAll
    static void initFxToolkit() {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Skipping JavaFX tests in headless environment");
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX already started.
        }
    }

    @Test
    public void constructor_normalisesPathBeforeDisplaying() throws Exception {
        Path rawPath = Paths.get("data", "..", "data", "taconnect.json");

        StatusBarFooter footer = createFooter(rawPath);
        Label saveLocationLabel = getSaveLocationLabel(footer);

        assertEquals(Paths.get("data", "taconnect.json").toString(), saveLocationLabel.getText());
    }

    private StatusBarFooter createFooter(Path saveLocation) throws Exception {
        StatusBarFooter[] holder = new StatusBarFooter[1];
        runFxAndWait(() -> holder[0] = new StatusBarFooter(saveLocation));
        return holder[0];
    }

    private Label getSaveLocationLabel(StatusBarFooter footer) throws Exception {
        Field field = StatusBarFooter.class.getDeclaredField("saveLocationStatus");
        field.setAccessible(true);
        return (Label) field.get(footer);
    }

    private static void runFxAndWait(Runnable runnable) throws Exception {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timed out waiting for FX task");
        }
    }
}

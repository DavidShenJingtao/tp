package seedu.address;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class MainAppVersionTest {
    @Test
    public void versionConstant_isReachable() {
        // Touches MainApp class init via field access (even if you don't assert exact numbers).
        assertNotNull(MainApp.VERSION);
    }
}

package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.exception.FormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLoadHandlerTest {

    private DefaultLoadHandler defaultLoadHandler;

    @BeforeEach
    void setUp() {
        defaultLoadHandler = new DefaultLoadHandler();
    }

    @Test
    void testHandleLoadThrowsFormatException() {
        File tempFile = null;

        try {
            tempFile = File.createTempFile("test", ".txt");

            // Use try-with-resources to ensure the writer is closed properly
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write("Invalid format content");
            }

            // Declare a final reference for lambda usage
            final File finalTempFile = tempFile;

            // Execute the method and assert the exception
            FormatException exception = assertThrows(FormatException.class, () -> {
                defaultLoadHandler.handleLoad(finalTempFile);
            });

            // Assert exception message
            assertEquals("Invalid format content", exception.getMessage());
        } catch (IOException e) {
            fail("Test setup failed due to IOException: " + e.getMessage());
        } finally {
            // Cleanup temporary file
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    System.err.println("Failed to delete the temporary file: " + tempFile.getAbsolutePath());
                }
            }
        }
    }

}

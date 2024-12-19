package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSaveAsHandlerTest {

    @Test
    void testCanHandle() {
        DefaultSaveAsHandler handler = new DefaultSaveAsHandler();
        assertFalse(handler.canHandle("anyFormat"), "DefaultSaveAsHandler should not handle any format.");
    }

    @Test
    void testHandleSaveAsThrowsException() {
        DefaultSaveAsHandler handler = new DefaultSaveAsHandler();

        // Create a valid Image object
        int[][] pixels = {{0, 1}, {1, 0}}; // Example pixel data
        File dummyFile = new File("dummyPath");
        Image dummyImage = new Image(2, 2, pixels, "PGM", dummyFile);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.handleSaveAs(dummyImage, dummyFile, "unsupportedFormat");
        });

        assertEquals("Unsupported target format: unsupportedFormat", exception.getMessage());
    }
}

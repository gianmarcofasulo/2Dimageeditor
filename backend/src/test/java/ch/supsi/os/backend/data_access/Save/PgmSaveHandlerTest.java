package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PgmSaveHandlerTest {

    private PgmSaveHandler pgmSaveHandler;

    @BeforeEach
    void setup() {
        pgmSaveHandler = new PgmSaveHandler();
    }

    @Test
    void testSavePgmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".pgm");
        Image pgmImage = new Image(2, 2, new int[][]{{128, 255}, {0, 64}}, "PGM", tempFile);

        boolean result = pgmSaveHandler.save(pgmImage, tempFile);

        assertTrue(result, "The PGM image should be saved successfully.");
        assertTrue(tempFile.exists(), "The file should exist.");
        tempFile.deleteOnExit(); // Cleanup
    }

    @Test
    void testSaveNonPgmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".pgm");
        Image nonPgmImage = new Image(2, 2, new int[][]{{128, 255}, {0, 64}}, "PPM", tempFile);

        boolean result = pgmSaveHandler.save(nonPgmImage, tempFile);

        assertFalse(result, "The PGM handler should pass unsupported formats to the next handler.");
        tempFile.deleteOnExit(); // Cleanup
    }
}

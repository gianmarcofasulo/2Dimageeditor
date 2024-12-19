package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PpmSaveHandlerTest {

    private PpmSaveHandler ppmSaveHandler;

    @BeforeEach
    void setup() {
        ppmSaveHandler = new PpmSaveHandler();
    }

    @Test
    void testSavePpmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".ppm");
        // Matrice con tre valori (RGB) per ogni pixel
        int[][] ppmPixels = {
                {255, 0, 0, 0, 255, 0},   // Riga 1: Rosso, Verde
                {0, 0, 255, 255, 255, 255} // Riga 2: Blu, Bianco
        };
        Image ppmImage = new Image(2, 2, ppmPixels, "PPM", tempFile);

        boolean result = ppmSaveHandler.save(ppmImage, tempFile);

        assertTrue(result, "The PPM image should be saved successfully.");
        assertTrue(tempFile.exists(), "The file should exist.");
        tempFile.deleteOnExit(); // Cleanup
    }

    @Test
    void testSaveNonPpmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".ppm");
        Image nonPpmImage = new Image(2, 2, new int[][]{{128, 255}, {0, 64}}, "PGM", tempFile);

        boolean result = ppmSaveHandler.save(nonPpmImage, tempFile);

        assertFalse(result, "The PPM handler should pass unsupported formats to the next handler.");
        tempFile.deleteOnExit(); // Cleanup
    }
}

package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveAsPpmHandlerTest {

    private SaveAsPpmHandler saveAsPpmHandler;

    @BeforeEach
    void setup() {
        saveAsPpmHandler = new SaveAsPpmHandler();
    }

    @Test
    void testHandleSaveAsPbmToPpm() throws IOException {
        int[][] pbmPixels = {{1, 0}, {0, 1}};
        Image pbmImage = new Image(2, 2, pbmPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".ppm");
        boolean result = saveAsPpmHandler.handleSaveAs(pbmImage, targetFile, "PPM");

        assertTrue(result, "La conversione e il salvataggio da PBM a PPM dovrebbe avere successo.");
        assertTrue(targetFile.exists(), "Il file convertito dovrebbe esistere.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPgmToPpm() throws IOException {
        int[][] pgmPixels = {{128, 255}, {0, 64}};
        Image pgmImage = new Image(2, 2, pgmPixels, "PGM", File.createTempFile("test", ".pgm"));

        File targetFile = File.createTempFile("test", ".ppm");
        boolean result = saveAsPpmHandler.handleSaveAs(pgmImage, targetFile, "PPM");

        assertTrue(result, "La conversione e il salvataggio da PGM a PPM dovrebbe avere successo.");
        assertTrue(targetFile.exists(), "Il file convertito dovrebbe esistere.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPpmToPpm() throws IOException {
        int[][] ppmPixels = {{255, 0, 0, 0, 255, 0}, {0, 0, 255, 255, 255, 255}};
        Image ppmImage = new Image(2, 2, ppmPixels, "PPM", File.createTempFile("test", ".ppm"));

        File targetFile = File.createTempFile("test", ".ppm");
        boolean result = saveAsPpmHandler.handleSaveAs(ppmImage, targetFile, "PPM");

        assertTrue(result, "Il salvataggio diretto di un'immagine PPM dovrebbe avere successo.");
        assertTrue(targetFile.exists(), "Il file convertito dovrebbe esistere.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsEmptyImage() throws IOException {
        int[][] emptyPixels = {};
        Image emptyImage = new Image(0, 0, emptyPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".ppm");
        boolean result = saveAsPpmHandler.handleSaveAs(emptyImage, targetFile, "PPM");

        assertTrue(result, "Saving an empty image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsSinglePixelImage() throws IOException {
        int[][] singlePixel = {{255}};
        Image singlePixelImage = new Image(1, 1, singlePixel, "PGM", File.createTempFile("test", ".pgm"));

        File targetFile = File.createTempFile("test", ".ppm");
        boolean result = saveAsPpmHandler.handleSaveAs(singlePixelImage, targetFile, "PPM");

        assertTrue(result, "Saving a single-pixel image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsLargeImage() throws IOException {
        int size = 1000; // Large image size
        int[][] largePixels = new int[size][size * 3]; // PPM expects RGB triplets
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 3; j++) {
                largePixels[i][j] = 128; // Assign a grayscale-equivalent RGB value
            }
        }

        Image largeImage = new Image(size, size, largePixels, "PPM", File.createTempFile("test", ".ppm"));
        File targetFile = File.createTempFile("test", ".ppm");

        try {
            boolean result = saveAsPpmHandler.handleSaveAs(largeImage, targetFile, "PPM");

            assertTrue(result, "Saving a large image should succeed.");
            assertTrue(targetFile.exists(), "The converted file should exist.");
        } finally {
            targetFile.deleteOnExit();
        }
    }

    @Test
    void testCanHandleTargetFormatCaseSensitivity() {
        assertTrue(saveAsPpmHandler.canHandle("PPM"), "Handler should recognize PPM in uppercase.");
        assertTrue(saveAsPpmHandler.canHandle("ppm"), "Handler should recognize PPM in lowercase.");
        assertTrue(saveAsPpmHandler.canHandle("PpM"), "Handler should recognize PPM in mixed case.");
        assertFalse(saveAsPpmHandler.canHandle("unsupported"), "Handler should not recognize unsupported formats.");
    }
}

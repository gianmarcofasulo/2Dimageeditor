package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveAsPgmHandlerTest {

    private SaveAsPgmHandler saveAsPgmHandler;

    @BeforeEach
    void setup() {
        saveAsPgmHandler = new SaveAsPgmHandler();
    }

    @Test
    void testHandleSaveAsPbmToPgm() throws IOException {
        int[][] pbmPixels = {{1, 0}, {0, 1}};
        Image pbmImage = new Image(2, 2, pbmPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".pgm");
        boolean result = saveAsPgmHandler.handleSaveAs(pbmImage, targetFile, "PGM");

        assertTrue(result, "Conversion and saving from PBM to PGM should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPpmToPgm() throws IOException {
        int[][] ppmPixels = {{255, 0, 0, 0, 255, 0}, {0, 0, 255, 255, 255, 255}};
        Image ppmImage = new Image(2, 2, ppmPixels, "PPM", File.createTempFile("test", ".ppm"));

        File targetFile = File.createTempFile("test", ".pgm");
        boolean result = saveAsPgmHandler.handleSaveAs(ppmImage, targetFile, "PGM");

        assertTrue(result, "Conversion and saving from PPM to PGM should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPgmToPgm() throws IOException {
        int[][] pgmPixels = {{128, 255}, {0, 64}};
        Image pgmImage = new Image(2, 2, pgmPixels, "PGM", File.createTempFile("test", ".pgm"));

        File targetFile = File.createTempFile("test", ".pgm");
        boolean result = saveAsPgmHandler.handleSaveAs(pgmImage, targetFile, "PGM");

        assertTrue(result, "Direct saving of a PGM image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsEmptyImage() throws IOException {
        int[][] emptyPixels = {};
        Image emptyImage = new Image(0, 0, emptyPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".pgm");
        boolean result = saveAsPgmHandler.handleSaveAs(emptyImage, targetFile, "PGM");

        assertTrue(result, "Saving an empty image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testCanHandleTargetFormatCaseSensitivity() {
        assertTrue(saveAsPgmHandler.canHandle("PGM"), "Handler should recognize PGM in uppercase.");
        assertTrue(saveAsPgmHandler.canHandle("pgm"), "Handler should recognize PGM in lowercase.");
        assertTrue(saveAsPgmHandler.canHandle("PgM"), "Handler should recognize PGM in mixed case.");
        assertFalse(saveAsPgmHandler.canHandle("unsupported"), "Handler should not recognize unsupported formats.");
    }

    @Test
    void testHandleSaveAsSinglePixelImage() throws IOException {
        int[][] singlePixel = {{128}};
        Image singlePixelImage = new Image(1, 1, singlePixel, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".pgm");
        boolean result = saveAsPgmHandler.handleSaveAs(singlePixelImage, targetFile, "PGM");

        assertTrue(result, "Saving a single-pixel image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsLargeImage() throws IOException {
        int size = 1000; // Dimensions of the image
        int[][] largePixels = new int[size][size * 3]; // PPM images expect RGB triplets
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 3; j++) {
                largePixels[i][j] = 128; // Assign grayscale value (applies equally to RGB channels)
            }
        }

        Image largeImage = new Image(size, size, largePixels, "PPM", File.createTempFile("test", ".ppm"));
        File targetFile = File.createTempFile("test", ".pgm");

        try {
            boolean result = saveAsPgmHandler.handleSaveAs(largeImage, targetFile, "PGM");

            assertTrue(result, "Saving a large image should succeed.");
            assertTrue(targetFile.exists(), "The converted file should exist.");
        } finally {
            targetFile.deleteOnExit();
        }
    }
}

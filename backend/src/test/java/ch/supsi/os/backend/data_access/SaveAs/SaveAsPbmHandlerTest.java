package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveAsPbmHandlerTest {

    private SaveAsPbmHandler saveAsPbmHandler;

    @BeforeEach
    void setup() {
        saveAsPbmHandler = new SaveAsPbmHandler();
    }

    @Test
    void testHandleSaveAsPgmToPbm() throws IOException {
        int[][] pgmPixels = {{128, 255}, {0, 64}};
        Image pgmImage = new Image(2, 2, pgmPixels, "PGM", File.createTempFile("test", ".pgm"));

        File targetFile = File.createTempFile("test", ".pbm");
        boolean result = saveAsPbmHandler.handleSaveAs(pgmImage, targetFile, "PBM");

        assertTrue(result, "Conversion and saving from PGM to PBM should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPpmToPbm() throws IOException {
        int[][] ppmPixels = {{255, 0, 0, 0, 255, 0}, {0, 0, 255, 255, 255, 255}};
        Image ppmImage = new Image(2, 2, ppmPixels, "PPM", File.createTempFile("test", ".ppm"));

        File targetFile = File.createTempFile("test", ".pbm");
        boolean result = saveAsPbmHandler.handleSaveAs(ppmImage, targetFile, "PBM");

        assertTrue(result, "Conversion and saving from PPM to PBM should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsPbmToPbm() throws IOException {
        int[][] pbmPixels = {{1, 0}, {0, 1}};
        Image pbmImage = new Image(2, 2, pbmPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".pbm");
        boolean result = saveAsPbmHandler.handleSaveAs(pbmImage, targetFile, "PBM");

        assertTrue(result, "Direct saving of a PBM image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsUnsupportedTargetFormat() throws IOException {
        int[][] pbmPixels = {{1, 0}, {0, 1}};
        Image pbmImage = new Image(2, 2, pbmPixels, "PBM", File.createTempFile("test", ".pbm"));

        File targetFile = File.createTempFile("test", ".unsupported");
        boolean result = saveAsPbmHandler.handleSaveAs(pbmImage, targetFile, "Unsupported");

        assertFalse(result, "Saving to an unsupported format should fail.");
        targetFile.deleteOnExit();
    }

    @Test
    void testHandleSaveAsEmptyImage() throws IOException {
        int[][] emptyPixels = {};
        Image emptyImage = new Image(0, 0, emptyPixels, "PGM", File.createTempFile("test", ".pgm"));

        File targetFile = File.createTempFile("test", ".pbm");
        boolean result = saveAsPbmHandler.handleSaveAs(emptyImage, targetFile, "PBM");

        assertTrue(result, "Saving an empty image should succeed.");
        assertTrue(targetFile.exists(), "The converted file should exist.");
        targetFile.deleteOnExit();
    }

    @Test
    void testCanHandleTargetFormatCaseSensitivity() {
        assertTrue(saveAsPbmHandler.canHandle("PBM"), "Handler should recognize PBM in uppercase.");
        assertTrue(saveAsPbmHandler.canHandle("pbm"), "Handler should recognize PBM in lowercase.");
        assertTrue(saveAsPbmHandler.canHandle("PbM"), "Handler should recognize PBM in mixed case.");
        assertFalse(saveAsPbmHandler.canHandle("unsupported"), "Handler should not recognize unsupported formats.");
    }
}

package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageSaverChainTest {

    private ImageSaverChain imageSaverChain;

    @BeforeEach
    void setup() {
        imageSaverChain = new ImageSaverChain();
    }

    @Test
    void testSaveSupportedPbmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".pbm");
        Image pbmImage = new Image(2, 2, new int[][]{{1, 0}, {0, 1}}, "PBM", tempFile);

        boolean result = imageSaverChain.saveImage(pbmImage, tempFile);

        assertTrue(result, "The chain should save a PBM image successfully.");
        tempFile.deleteOnExit(); // Cleanup
    }

    @Test
    void testSaveUnsupportedFormat() throws IOException {
        File tempFile = File.createTempFile("test", ".unknown");
        Image unsupportedImage = new Image(2, 2, new int[][]{{128, 255}, {0, 64}}, "UNKNOWN", tempFile);

        boolean result = imageSaverChain.saveImage(unsupportedImage, tempFile);

        assertFalse(result, "The chain should not handle unsupported formats.");
        tempFile.deleteOnExit(); // Cleanup
    }
}

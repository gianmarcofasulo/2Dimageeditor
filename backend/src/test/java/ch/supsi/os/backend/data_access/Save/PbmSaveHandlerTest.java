package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class PbmSaveHandlerTest {

    private PbmSaveHandler pbmSaveHandler;

    @BeforeEach
    void setup() {
        pbmSaveHandler = new PbmSaveHandler();
    }

    @Test
    void testSavePbmImage() throws IOException {
        File tempFile = File.createTempFile("test", ".pbm");
        Image pbmImage = new Image(2, 2,new int[][]{{1, 0}, {0, 1}}, "PBM", tempFile);

        boolean result = pbmSaveHandler.save(pbmImage, tempFile);

        assertTrue(result, "The PBM image should be saved successfully.");
        assertTrue(tempFile.exists(), "The file should exist.");
        tempFile.deleteOnExit(); // Cleanup
    }
}

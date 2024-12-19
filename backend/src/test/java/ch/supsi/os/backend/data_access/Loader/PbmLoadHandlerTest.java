package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PbmLoadHandlerTest {

    private PbmLoadHandler pbmLoadHandler;

    @BeforeEach
    void setUp() {
        pbmLoadHandler = new PbmLoadHandler();
    }

    @Test
    void testHandleLoadValidPbmFile() throws IOException {
        // Create a temporary PBM file
        File tempFile = File.createTempFile("test", ".pbm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P1\n");
            writer.write("# This is a comment\n");
            writer.write("3 2\n");
            writer.write("0 1 0\n");
            writer.write("1 0 1\n");
        }

        // Test the PBM loader
        Image image = pbmLoadHandler.handleLoad(tempFile);

        assertNotNull(image, "Image should not be null for a valid PBM file");
        assertEquals(3, image.getWidth(), "Image width should be 3");
        assertEquals(2, image.getHeight(), "Image height should be 2");
        assertArrayEquals(
                new int[][]{
                        {0, 1, 0},
                        {1, 0, 1}
                },
                image.getPixels(),
                "Image pixels do not match"
        );
        assertEquals("PBM", image.getFormat(), "Image format should be PBM");
    }

    @Test
    void testHandleLoadInvalidPbmFile() throws IOException {
        // Create a temporary file with invalid PBM content
        File tempFile = File.createTempFile("test", ".pbm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P2\n"); // Invalid PBM format identifier
            writer.write("3 2\n");
            writer.write("0 1 0\n");
            writer.write("1 0 1\n");
        }

        // Test the PBM loader
        Image image = pbmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for an invalid PBM file");
    }

    @Test
    void testHandleLoadNonPbmFile() throws IOException {
        // Create a temporary non-PBM file
        File tempFile = File.createTempFile("test", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is not a PBM file\n");
        }

        // Test the PBM loader
        Image image = pbmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for a non-PBM file");
    }
}

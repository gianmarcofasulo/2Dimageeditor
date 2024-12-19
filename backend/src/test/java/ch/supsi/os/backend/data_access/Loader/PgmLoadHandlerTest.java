package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PgmLoadHandlerTest {

    private PgmLoadHandler pgmLoadHandler;

    @BeforeEach
    void setUp() {
        pgmLoadHandler = new PgmLoadHandler();
    }

    @Test
    void testHandleLoadValidPgmFile() throws IOException {
        // Create a temporary PGM file
        File tempFile = File.createTempFile("test", ".pgm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P2\n");
            writer.write("# This is a comment\n");
            writer.write("3 2\n");
            writer.write("255\n");
            writer.write("0 127 255\n");
            writer.write("64 128 192\n");
        }

        // Test the PGM loader
        Image image = pgmLoadHandler.handleLoad(tempFile);

        assertNotNull(image, "Image should not be null for a valid PGM file");
        assertEquals(3, image.getWidth(), "Image width should be 3");
        assertEquals(2, image.getHeight(), "Image height should be 2");
        assertArrayEquals(
                new int[][]{
                        {0, 127, 255},
                        {64, 128, 192}
                },
                image.getPixels(),
                "Image pixels do not match"
        );
        assertEquals("PGM", image.getFormat(), "Image format should be PGM");
    }

    @Test
    void testHandleLoadInvalidPgmFile() throws IOException {
        // Create a temporary file with invalid PGM content
        File tempFile = File.createTempFile("test", ".pgm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P3\n"); // Invalid PGM format identifier
            writer.write("3 2\n");
            writer.write("255\n");
            writer.write("0 127 255\n");
            writer.write("64 128 192\n");
        }

        // Test the PGM loader
        Image image = pgmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for an invalid PGM file");
    }

    @Test
    void testHandleLoadNonPgmFile() throws IOException {
        // Create a temporary non-PGM file
        File tempFile = File.createTempFile("test", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is not a PGM file\n");
        }

        // Test the PGM loader
        Image image = pgmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for a non-PGM file");
    }
}

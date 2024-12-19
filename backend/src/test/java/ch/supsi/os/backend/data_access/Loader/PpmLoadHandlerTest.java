package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PpmLoadHandlerTest {

    private PpmLoadHandler ppmLoadHandler;

    @BeforeEach
    void setUp() {
        ppmLoadHandler = new PpmLoadHandler();
    }

    @Test
    void testHandleLoadValidPpmFile() throws IOException {
        // Create a temporary PPM file
        File tempFile = File.createTempFile("test", ".ppm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P3\n");
            writer.write("# This is a comment\n");
            writer.write("2 2\n");
            writer.write("255\n");
            writer.write("255 0 0  0 255 0\n");
            writer.write("0 0 255  255 255 255\n");
        }

        // Test the PPM loader
        Image image = ppmLoadHandler.handleLoad(tempFile);

        assertNotNull(image, "Image should not be null for a valid PPM file");
        assertEquals(2, image.getWidth(), "Image width should be 2");
        assertEquals(2, image.getHeight(), "Image height should be 2");
        assertArrayEquals(
                new int[][]{
                        {255, 0, 0, 0, 255, 0},
                        {0, 0, 255, 255, 255, 255}
                },
                image.getPixels(),
                "Image pixels do not match"
        );
        assertEquals("PPM", image.getFormat(), "Image format should be PPM");
    }

    @Test
    void testHandleLoadInvalidPpmFile() throws IOException {
        // Create a temporary file with invalid PPM content
        File tempFile = File.createTempFile("test", ".ppm");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("P2\n"); // Invalid PPM format identifier
            writer.write("2 2\n");
            writer.write("255\n");
            writer.write("255 0 0 0 255 0\n");
            writer.write("0 0 255 255 255 255\n");
        }

        // Test the PPM loader
        Image image = ppmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for an invalid PPM file");
    }

    @Test
    void testHandleLoadNonPpmFile() throws IOException {
        // Create a temporary non-PPM file
        File tempFile = File.createTempFile("test", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is not a PPM file\n");
        }

        // Test the PPM loader
        Image image = ppmLoadHandler.handleLoad(tempFile);

        assertNull(image, "Image should be null for a non-PPM file");
    }
}

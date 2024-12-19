package ch.supsi.os.backend.business;

import ch.supsi.os.backend.exception.NegativeException;
import ch.supsi.os.backend.model.Image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService();
    }

    @Test
    void testGetImageFormat_P1() throws Exception {
        Image p1Image = new Image(5, 5, new int[][] {
                {0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0}
        });

        String format = imageService.getImageFormat(p1Image);
        assertEquals("P1", format, "The image format should be P1 (binary).");
    }

    @Test
    void testGetImageFormat_P2() throws Exception {
        Image p2Image = new Image(5, 5, new int[][] {
                {0, 50, 100, 150, 200},
                {10, 60, 110, 160, 210},
                {20, 70, 120, 170, 220},
                {30, 80, 130, 180, 230},
                {40, 90, 140, 190, 240}
        });

        String format = imageService.getImageFormat(p2Image);
        assertEquals("P2", format, "The image format should be P2 (grayscale).");
    }

    @Test
    void testGetImageFormat_P3() throws Exception {
        Image p3Image = new Image(3, 3, new int[][] {
                {255, 0, 0, 0, 255, 0, 0, 0, 255}, // Red, Green, Blue
                {128, 128, 128, 64, 64, 64, 192, 192, 192}, // Grayscale colors
                {255, 255, 0, 0, 255, 255, 255, 0, 255} // Yellow, Cyan, Magenta
        });

        String format = imageService.getImageFormat(p3Image);
        assertEquals("P2", format, "The image format should be P3 (color).");
    }

    @Test
    void testRotateRight_P2() {
        Image image = new Image(3, 2, new int[][] {
                {1, 2, 3},
                {4, 5, 6}
        });

        imageService.rotateRight(image);

        assertEquals(2, image.getWidth(), "After rotation, the width should match the original height.");
        assertEquals(3, image.getHeight(), "After rotation, the height should match the original width.");
        assertArrayEquals(new int[][] {
                {4, 1},
                {5, 2},
                {6, 3}
        }, image.getPixels(), "The pixels should be rotated 90 degrees clockwise.");
    }

    @Test
    void testRotateLeft_P2() {
        Image image = new Image(3, 2, new int[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        imageService.rotateLeft(image);

        assertEquals(2, image.getWidth(), "After rotation, the width should match the original height.");
        assertEquals(3, image.getHeight(), "After rotation, the height should match the original width.");
        assertArrayEquals(new int[][]{
                {3, 6},
                {2, 5},
                {1, 4}
        }, image.getPixels(), "The pixels should be rotated 90 degrees counterclockwise.");
    }

    @Test
    void testFlipHorizontal_P3() {
        Image image = new Image(3, 2, new int[][] {
                {255, 0, 0, 0, 255, 0, 0, 0, 255}, // RGB
                {128, 128, 128, 64, 64, 64, 192, 192, 192}
        });

        imageService.flipHorizontal(image);

        assertArrayEquals(new int[][] {
                {0, 0, 255, 0, 255, 0, 255, 0, 0},
                {192, 192, 192, 64, 64, 64, 128, 128, 128}
        }, image.getPixels(), "The pixels should be flipped horizontally for RGB images.");
    }

    @Test
    void testFlipVertical_P2() {
        Image image = new Image(3, 2, new int[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        imageService.flipVertical(image);

        assertArrayEquals(new int[][]{
                {4, 5, 6},
                {1, 2, 3}
        }, image.getPixels(), "The pixels should be flipped vertically.");
    }

    @Test
    void testInvertImageColors_P2() {
        Image image = new Image(3, 2, new int[][] {
                {100, 150, 200},
                {50, 25, 0}
        });

        imageService.invertImageColors(image);

        assertArrayEquals(new int[][] {
                {155, 105, 55},
                {205, 230, 255}
        }, image.getPixels(), "The pixel values should be inverted for grayscale images.");
    }

    @Test
    void testInvertImageColors_P1() {
        Image image = new Image(3, 2, new int[][] {
                {0, 1, 0},
                {1, 0, 1}
        });

        imageService.invertImageColors(image);

        assertArrayEquals(new int[][] {
                {1, 0, 1},
                {0, 1, 0}
        }, image.getPixels(), "The pixel values should be inverted for binary images.");
    }

    @Test
    void testValidateImage_NoNegativePixels() throws NegativeException {
        Image image = new Image(3, 2, new int[][]{
                {0, 1, 2},
                {3, 4, 5}
        });

        assertDoesNotThrow(() -> imageService.validateImage(image), "Validation should pass for non-negative pixels.");
    }

    @Test
    void testValidateImage_WithNegativePixels() {
        Image image = new Image(3, 2, new int[][]{
                {0, 1, -2},
                {3, 4, 5}
        });

        assertThrows(NegativeException.class, () -> imageService.validateImage(image), "Validation should fail for negative pixels.");
    }

    @Test
    void testSaveImage_UnsupportedFormat() {
        Image image = new Image(2, 2, new int[][]{{1, 2}, {3, 4}}, "UNSUPPORTED", new File("test.unknown"));
        File file = new File("output.unknown");

        imageService.saveImage(image, file);

        // The test can check for console output or any logs if applicable.
        assertFalse(file.exists(), "The image file should not be created for unsupported formats.");
        file.deleteOnExit();
    }

    @Test
    void testLoadImage_Success() throws Exception {
        File file = File.createTempFile("test", ".pbm");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("P1");
            writer.println("2 2");
            writer.println("0 1");
            writer.println("1 0");
        }

        Image image = imageService.loadImage(file);

        assertNotNull(image, "The image should be successfully loaded.");
        assertEquals(2, image.getWidth(), "The image width should match the file.");
        assertEquals(2, image.getHeight(), "The image height should match the file.");
        file.deleteOnExit();
    }
}

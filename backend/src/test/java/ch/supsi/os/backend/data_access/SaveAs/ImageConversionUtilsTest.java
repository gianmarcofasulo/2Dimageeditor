package ch.supsi.os.backend.data_access.SaveAs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageConversionUtilsTest {

    @Test
    void testConvertP1ToP2() {
        int[][] input = {
                {1, 0},
                {0, 1}
        };
        int[][] expected = {
                {0, 255},
                {255, 0}
        };
        int[][] result = ImageConversionUtils.convertP1ToP2(input, 2, 2);
        assertArrayEquals(expected, result, "Conversione P1 -> P2 fallita");
    }

    @Test
    void testConvertP1ToP3() {
        int[][] input = {
                {1, 0},
                {0, 1}
        };
        int[][] expected = {
                {0, 0, 0, 255, 255, 255},
                {255, 255, 255, 0, 0, 0}
        };
        int[][] result = ImageConversionUtils.convertP1ToP3(input, 2, 2);
        assertArrayEquals(expected, result, "Conversione P1 -> P3 fallita");
    }

    @Test
    void testConvertP2ToP1() {
        int[][] input = {
                {128, 255},
                {127, 0}
        };
        int[][] expected = {
                {0, 0},
                {1, 1}
        };
        int[][] result = ImageConversionUtils.convertP2ToP1(input, 2, 2);
        assertArrayEquals(expected, result, "Conversione P2 -> P1 fallita");
    }

    @Test
    void testConvertP2ToP3() {
        int[][] input = {
                {128, 255},
                {0, 64}
        };
        int[][] expected = {
                {128, 128, 128, 255, 255, 255},
                {0, 0, 0, 64, 64, 64}
        };
        int[][] result = ImageConversionUtils.convertP2ToP3(input, 2, 2);
        assertArrayEquals(expected, result, "Conversione P2 -> P3 fallita");
    }

    @Test
    void testConvertP3ToP1() {
        int[][] input = {
                {255, 255, 255, 0, 0, 0},
                {128, 128, 128, 64, 64, 64}
        };
        int[][] expected = {
                {0, 1},
                {0, 1}
        };
        int[][] result = ImageConversionUtils.convertP3ToP1(input, 2, 2);
        assertArrayEquals(expected, result, "Conversione P3 -> P1 fallita");
    }

    @Test
    void testConvertP3ToP2() {
        int[][] input = {
                {255, 0, 0, 0, 255, 0},
                {0, 0, 255, 255, 255, 255}
        };
        int[][] expected = {
                {76, 150},
                {29, 255}
        };
        int[][] result = ImageConversionUtils.convertP3ToP2(input, 2, 2);

        // Confronto con tolleranza
        assertArrayEqualsWithTolerance(expected, result, 1, "Conversione P3 -> P2 fallita");
    }

    // Metodo helper per il confronto con tolleranza
    void assertArrayEqualsWithTolerance(int[][] expected, int[][] actual, int tolerance, String message) {
        assertEquals(expected.length, actual.length, message + ": Lunghezze diverse");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].length, actual[i].length, message + ": Larghezze diverse nella riga " + i);
            for (int j = 0; j < expected[i].length; j++) {
                assertTrue(Math.abs(expected[i][j] - actual[i][j]) <= tolerance,
                        message + ": Valore diverso alla posizione [" + i + "][" + j + "]. Expected: " + expected[i][j] + ", Actual: " + actual[i][j]);
            }
        }
    }


}

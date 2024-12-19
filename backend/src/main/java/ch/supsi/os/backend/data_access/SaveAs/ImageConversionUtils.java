package ch.supsi.os.backend.data_access.SaveAs;

public class ImageConversionUtils {

    // Convert from P1 (Binary) to P2 (Grayscale)
    public static int[][] convertP1ToP2(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // P1: 1 becomes 255 (white), 0 becomes 0 (black)
                converted[y][x] = (pixels[y][x] == 1) ? 0 : 255;
            }
        }
        return converted;
    }

    // Convert from P1 (Binary) to P3 (RGB)
    public static int[][] convertP1ToP3(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width * 3];  // 3 values per pixel (RGB)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = pixels[y][x];
                // P1: 1 becomes white (255, 255, 255), 0 becomes black (0, 0, 0)
                int color = (value == 1) ? 0 : 255;

                if (x * 3 >= width * 3) {
                    System.out.println("Index out of bounds: " + (x * 3));
                }
                converted[y][x * 3] = color;     // Red channel
                converted[y][x * 3 + 1] = color; // Green channel
                converted[y][x * 3 + 2] = color; // Blue channel
            }
        }
        return converted;
    }

    // Convert from P2 (Grayscale) to P1 (Binary)
    public static int[][] convertP2ToP1(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // P2: Threshold grayscale to binary
                converted[y][x] = (pixels[y][x] >= 128) ? 0 : 1; // 1 for black, 0 for white
            }
        }
        return converted;
    }

    // Convert from P2 (Grayscale) to P3 (RGB)
    public static int[][] convertP2ToP3(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width * 3];  // 3 values per pixel (RGB)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = pixels[y][x];
                // P2: Convert grayscale to RGB (R = G = B = gray)
                converted[y][x * 3] = gray;      // Red channel
                converted[y][x * 3 + 1] = gray;  // Green channel
                converted[y][x * 3 + 2] = gray;  // Blue channel
            }
        }
        return converted;
    }

    // Convert from P3 (RGB) to P1 (Binary)
    public static int[][] convertP3ToP1(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = pixels[y][x * 3];
                int g = pixels[y][x * 3 + 1];
                int b = pixels[y][x * 3 + 2];

                int average = (r + g + b) / 3;
                converted[y][x] = (average >= 128) ? 0 : 1;  // 1 for black, 0 for white
            }
        }
        return converted;
    }

    // Convert from P3 (RGB) to P2 (Grayscale)
    public static int[][] convertP3ToP2(int[][] pixels, int width, int height) {
        int[][] converted = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = pixels[y][x * 3];
                int g = pixels[y][x * 3 + 1];
                int b = pixels[y][x * 3 + 2];
                // P3: Convert RGB to grayscale using luminosity method
                int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                converted[y][x] = gray;
            }
        }
        return converted;
    }
}

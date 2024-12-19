package ch.supsi.os.backend.business;

import ch.supsi.os.backend.data_access.Loader.ImageLoaderChain;
import ch.supsi.os.backend.data_access.Save.ImageSaverChain;
import ch.supsi.os.backend.data_access.SaveAs.SaveAsChain;
import ch.supsi.os.backend.exception.FormatException;
import ch.supsi.os.backend.exception.NegativeException;
import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class ImageService {

    private final ImageLoaderChain imageLoaderChain;
    private final ImageSaverChain imageSaverChain;
    private final SaveAsChain saveAsChain;

    public ImageService() {
        this.imageLoaderChain = new ImageLoaderChain();
        this.imageSaverChain = new ImageSaverChain(); // Configura la catena
        this.saveAsChain = new SaveAsChain(); // Configura la catena
    }

    public void saveImage(Image image, File file) {
        try {
            boolean success = imageSaverChain.saveImage(image, file);
            if (!success) {
                System.out.println("Errore: il salvataggio dell'immagine è fallito. Formato non supportato.");
            } else {
                System.out.println("Salvataggio completato con successo.");
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio dell'immagine: " + e.getMessage());
        }
    }

    //save as P1, P2, P3
    public void saveImageAs(Image image, File file, String targetFormat) {
        try {
            boolean success = saveAsChain.saveAs(image, file, targetFormat);
            if (!success) {
                System.out.println("Errore: il salvataggio dell'immagine è fallito. Formato non supportato.");
            } else {
                System.out.println("Salvataggio completato con successo.");
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio dell'immagine: " + e.getMessage());
        }
    }

    /**
     * Carica un'immagine da un file usando la catena di loader.
     *
     * @param file Il file da caricare.
     * @return L'immagine caricata.
     * @throws IOException Se il caricamento fallisce.
     */
    public Image loadImage(File file) throws IOException, FormatException {
        return imageLoaderChain.getHandlerChain().loadImage(file);
    }

    public void validateImage(Image image) throws NegativeException {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getPixels()[y][x] < 0) {
                    throw new NegativeException("");
                }
            }
        }
    }

    // get the image format (P1, P2, P3) from the file header
    public String getImageFormat(Image image) throws FormatException {
        int[][] pixels = image.getPixels();

        // Determina se l'immagine è binaria (P1)
        boolean isBinary = true;
        for (int[] row : pixels) {
            for (int pixel : row) {
                if (pixel != 0 && pixel != 1) {  // Solo 0 e 1 sono ammessi per immagini binarie
                    isBinary = false;
                    break;
                }
            }
            if (!isBinary) break;
        }

        if (isBinary) {
            return "P1"; // Formato bianco e nero
        } else {
            // Se l'immagine non è binaria, determinare se è in scala di grigi (P2) o a colori (P3)
            int maxPixelValue = findMaxPixelValue(pixels);
            if (maxPixelValue <= 255) {
                return "P2"; // Formato scala di grigi
            } else {
                return "P3"; // Formato a colori
            }
        }
    }

    // Find the maximum pixel value in the image
    private int findMaxPixelValue(int[][] pixels) {
        int max = 0;
        for (int[] row : pixels) {
            for (int pixel : row) {
                if (pixel > max) {
                    max = pixel;
                }
            }
        }
        return max;
    }

    // Rotates an image 90 degrees clockwise
    public void rotateRight(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] originalPixels = image.getPixels();

        // Check if the image is P3 (RGB) format by verifying if each row has three times the width
        boolean isP3 = originalPixels[0].length == width * 3;

        // Temporary array for rotated pixels
        int[][] rotatedPixels;

        if (isP3) {
            rotatedPixels = new int[width][height * 3];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int newRow = x;
                    int newCol = height - y - 1;
                    rotatedPixels[newRow][newCol * 3] = originalPixels[y][x * 3];         // Red
                    rotatedPixels[newRow][newCol * 3 + 1] = originalPixels[y][x * 3 + 1]; // Green
                    rotatedPixels[newRow][newCol * 3 + 2] = originalPixels[y][x * 3 + 2]; // Blue
                }
            }
        } else {
            rotatedPixels = new int[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedPixels[x][height - y - 1] = originalPixels[y][x];
                }
            }
        }

        // Update the image with rotated pixels and swapped dimensions
        image.setPixels(rotatedPixels);
        image.setWidth(height);
        image.setHeight(width);
    }

    // Rotates an image 90 degrees counterclockwise
    public void rotateLeft(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] originalPixels = image.getPixels();

        boolean isP3 = originalPixels[0].length == width * 3;
        int[][] rotatedPixels;

        if (isP3) {
            rotatedPixels = new int[width][height * 3];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int newRow = width - x - 1;
                    int newCol = y;
                    rotatedPixels[newRow][newCol * 3] = originalPixels[y][x * 3];         // Red
                    rotatedPixels[newRow][newCol * 3 + 1] = originalPixels[y][x * 3 + 1]; // Green
                    rotatedPixels[newRow][newCol * 3 + 2] = originalPixels[y][x * 3 + 2]; // Blue
                }
            }
        } else {
            rotatedPixels = new int[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedPixels[width - x - 1][y] = originalPixels[y][x];
                }
            }
        }

        image.setPixels(rotatedPixels);
        image.setWidth(height);
        image.setHeight(width);
    }

    // Flips an image horizontally
    public void flipHorizontal(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = image.getPixels();

        boolean isP3 = pixels[0].length == width * 3;

        if (isP3) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width / 2; x++) {
                    int opposite = width - x - 1;

                    // Swap each color channel for RGB
                    int tempRed = pixels[y][x * 3];
                    int tempGreen = pixels[y][x * 3 + 1];
                    int tempBlue = pixels[y][x * 3 + 2];

                    pixels[y][x * 3] = pixels[y][opposite * 3];
                    pixels[y][x * 3 + 1] = pixels[y][opposite * 3 + 1];
                    pixels[y][x * 3 + 2] = pixels[y][opposite * 3 + 2];

                    pixels[y][opposite * 3] = tempRed;
                    pixels[y][opposite * 3 + 1] = tempGreen;
                    pixels[y][opposite * 3 + 2] = tempBlue;
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width / 2; x++) {
                    int opposite = width - x - 1;

                    // Swap pixels for grayscale or binary images
                    int temp = pixels[y][x];
                    pixels[y][x] = pixels[y][opposite];
                    pixels[y][opposite] = temp;
                }
            }
        }

        image.setPixels(pixels);  // Update the pixels in place
    }

    // Flips an image vertically
    public void flipVertical(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = image.getPixels();

        boolean isP3 = pixels[0].length == width * 3;

        if (isP3) {
            for (int y = 0; y < height / 2; y++) {
                int opposite = height - y - 1;
                for (int x = 0; x < width; x++) {
                    // Swap each color channel for RGB
                    int tempRed = pixels[y][x * 3];
                    int tempGreen = pixels[y][x * 3 + 1];
                    int tempBlue = pixels[y][x * 3 + 2];

                    pixels[y][x * 3] = pixels[opposite][x * 3];
                    pixels[y][x * 3 + 1] = pixels[opposite][x * 3 + 1];
                    pixels[y][x * 3 + 2] = pixels[opposite][x * 3 + 2];

                    pixels[opposite][x * 3] = tempRed;
                    pixels[opposite][x * 3 + 1] = tempGreen;
                    pixels[opposite][x * 3 + 2] = tempBlue;
                }
            }
        } else {
            for (int y = 0; y < height / 2; y++) {
                int opposite = height - y - 1;
                for (int x = 0; x < width; x++) {
                    // Swap pixels for grayscale or binary images
                    int temp = pixels[y][x];
                    pixels[y][x] = pixels[opposite][x];
                    pixels[opposite][x] = temp;
                }
            }
        }

        image.setPixels(pixels);  // Update the pixels in place
    }

    // Inverts the colors of the image (negative filter)
    public void invertImageColors(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = image.getPixels();  // Ottieni la matrice dei pixel esistente

        // Check if the image is P3 by verifying if each row has three times the width (for RGB)
        boolean isP3 = pixels[0].length == width * 3;

        if (isP3) {
            // P3 (RGB color image) inversion logic
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Invert each RGB channel in place
                    pixels[y][x * 3] = 255 - pixels[y][x * 3];         // Red
                    pixels[y][x * 3 + 1] = 255 - pixels[y][x * 3 + 1]; // Green
                    pixels[y][x * 3 + 2] = 255 - pixels[y][x * 3 + 2]; // Blue
                }
            }
        } else {
            // Determine if the image is P1 (binary) or P2 (grayscale)
            boolean isBinary = true;
            for (int y = 0; y < height && isBinary; y++) {
                for (int x = 0; x < width; x++) {
                    if (pixels[y][x] != 0 && pixels[y][x] != 1) {
                        isBinary = false;
                        break;
                    }
                }
            }

            if (isBinary) {
                // P1 (binary) inversion logic
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        pixels[y][x] = (pixels[y][x] == 1) ? 0 : 1;
                    }
                }
            } else {
                // P2 (grayscale) inversion logic
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        pixels[y][x] = 255 - pixels[y][x];
                    }
                }
            }
        }
    }

}


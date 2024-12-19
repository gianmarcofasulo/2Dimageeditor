package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.ImageService;
import ch.supsi.os.backend.exception.FormatException;
import ch.supsi.os.backend.model.Image;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;

public class ImageController {

    @FXML
    private Canvas canvas;

    private final ImageService imageService;
    private Image currentImage;
    private FilterMenuController filterMenuController;

    private static ImageController instance = null;

    public static ImageController getInstance() {
        if (instance == null) { // First check without locking
            synchronized (ImageController.class) {
                if (instance == null) { // Double-checked locking
                    instance = new ImageController();
                }
            }
        }
        return instance;
    }

    public ImageController() {
        this.imageService = new ImageService();  // Initialize the ImageService
    }

    @FXML
    public void initialize() {
    }

    public void setFilterMenuController(FilterMenuController filterMenuController) {
        this.filterMenuController = filterMenuController;
    }

    public void saveImage(Image image, File file) {
        imageService.saveImage(image, file);
    }

    public void saveAsImage(Image image, File file, String targetFormat) {
        imageService.saveImageAs(image, file, targetFormat);
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public String getImageFormat(Image image) throws FormatException, IOException {
        return imageService.getImageFormat(image);
    }

    public void drawImageOnCanvas(Image image) {
        if (canvas == null) {
            return;
        }
        this.currentImage = image; // Update current image reference
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Get the actual size of the canvas
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        int[][] pixels = image.getPixels();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Calculate the scaling factor to fit the image on the canvas while maintaining the aspect ratio
        double xScale = canvasWidth / imageWidth;
        double yScale = canvasHeight / imageHeight;

        // Optional: Limit scaling factor to a percentage of the canvas size (e.g., max 80% of the canvas)
        double maxScaleFactor = 0.8; // Allow image to fill only 80% of the canvas
        double scaleFactor = Math.min(xScale, yScale) * maxScaleFactor;

        // Calculate the new scaled image dimensions
        double newImageWidth = imageWidth * scaleFactor;
        double newImageHeight = imageHeight * scaleFactor;

        // Calculate the offset to center the image on the canvas
        double xOffset = (canvasWidth - newImageWidth) / 2;
        double yOffset = (canvasHeight - newImageHeight) / 2;

        // Determine if the image is in color (P3), grayscale (P2), or black and white (P1)
        boolean isColor = (pixels[0].length == imageWidth * 3);  // P3 has 3 values per pixel (RGB)
        boolean isGrayscale = false;
        boolean isBinary = true;

        // Check if all pixel values are 0 or 1 for P1
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (pixels[y][x] != 0 && pixels[y][x] != 1) {
                    isBinary = false;
                    break;
                }
            }
            if (!isBinary) break;
        }

        if (!isColor && !isBinary) {
            isGrayscale = true;  // It's P2 if not binary and not color
        }

        // Draw the scaled image pixel by pixel
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color color;

                if (isColor) {
                    // P3 Image (RGB Color)
                    int r = pixels[y][x * 3];       // Red component
                    int g = pixels[y][x * 3 + 1];   // Green component
                    int b = pixels[y][x * 3 + 2];
                    color = Color.rgb(r, g, b);
                } else if (isGrayscale) {
                    // P2 Image (Grayscale)
                    int gray = pixels[y][x];        // Grayscale value between 0 and 255
                    color = Color.grayRgb(gray);
                } else {
                    // P1 Image (Black and White)
                    int pixelValue = pixels[y][x];
                    color = (pixelValue == 1) ? Color.BLACK : Color.WHITE;
                }

                // Set the color and draw a scaled pixel
                gc.setFill(color);
                gc.fillRect(x * scaleFactor + xOffset, y * scaleFactor + yOffset, scaleFactor, scaleFactor);
            }
        }

        // Notifica che l'immagine è stata caricata
        if (filterMenuController != null) {
            filterMenuController.enableButtons();
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void rotateLeft(Image image) {
        imageService.rotateLeft(image);
    }

    public void rotateRight(Image image) {
        imageService.rotateRight(image);
    }

    public void flipHorizontal(Image image) {
        imageService.flipHorizontal(image);
    }

    public void flipVertical(Image image) {
        imageService.flipVertical(image);
    }

    public void negativeFilter(Image image) {
        imageService.invertImageColors(image);
    }

}

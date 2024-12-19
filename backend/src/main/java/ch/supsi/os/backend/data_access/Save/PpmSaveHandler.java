package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PpmSaveHandler extends BaseImageSaveHandler {
    @Override
    public boolean save(Image image, File file) throws IOException {
        if (!("PPM".equalsIgnoreCase(image.getFormat()) || "PPM FILE".equalsIgnoreCase(image.getFormat()))) {
            return saveNext(image, file); // Passa al prossimo handler
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Scrivi l'intestazione PPM
            writer.write("P3");
            writer.newLine();
            writer.write(image.getWidth() + " " + image.getHeight());
            writer.newLine();
            writer.write("255");
            writer.newLine();

            // Scrivi i dati dei pixel
            int[][] pixels = image.getPixels();
            for (int y = 0; y < image.getHeight(); y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x < image.getWidth(); x++) {
                    // Caso RGB
                    int r = pixels[y][x * 3];
                    int g = pixels[y][x * 3 + 1];
                    int b = pixels[y][x * 3 + 2];
                    line.append(r).append(" ").append(g).append(" ").append(b).append(" ");
                }
                writer.write(line.toString().trim());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
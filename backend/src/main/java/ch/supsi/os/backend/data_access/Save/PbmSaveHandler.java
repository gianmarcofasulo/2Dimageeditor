package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PbmSaveHandler extends BaseImageSaveHandler {
    @Override
    public boolean save(Image image, File file) throws IOException {

        if (!("PBM".equalsIgnoreCase(image.getFormat()) || "PBM FILE".equalsIgnoreCase(image.getFormat()))) {
            return saveNext(image, file); // Passa al prossimo handler
        }

        // Sovrascrive il file esistente
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("P1\n");
            writer.write(image.getWidth() + " " + image.getHeight() + "\n");

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    writer.write(image.getPixels()[y][x] + " ");
                }
                writer.write("\n");
            }
        }
        return true;
    }
}

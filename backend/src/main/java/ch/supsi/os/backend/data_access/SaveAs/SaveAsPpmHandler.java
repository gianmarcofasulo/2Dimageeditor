package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.data_access.Save.ImageSaverChain;
import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class SaveAsPpmHandler extends SaveAsHandler {
    @Override
    protected boolean canHandle(String format) {
        return "PPM".equalsIgnoreCase(format) || "PPM FILE".equalsIgnoreCase(format);
    }

    @Override
    public boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {
        int[][] convertedPixels;
        // Convert the image pixels depending on the current format of the image
        if ("PBM".equalsIgnoreCase(image.getFormat()) || "PBM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P1 to P3
            convertedPixels = ImageConversionUtils.convertP1ToP3(image.getPixels(), image.getWidth(), image.getHeight());
        } else if("PGM".equalsIgnoreCase(image.getFormat()) || "PGM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P2 to P3
            convertedPixels = ImageConversionUtils.convertP2ToP3(image.getPixels(), image.getWidth(), image.getHeight());
        } else {
            // Already in P3 format, no conversion needed
            convertedPixels = image.getPixels();
        }

        Image convertedImage = new Image(image.getWidth(), image.getHeight(), convertedPixels, targetFormat, file);

        // Use the existing ImageSaverChain to save the converted image
        return new ImageSaverChain().saveImage(convertedImage, file);
    }
}

package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.data_access.Save.ImageSaverChain;
import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class SaveAsPgmHandler extends SaveAsHandler {
    @Override
    protected boolean canHandle(String format) {
        return  "PGM".equalsIgnoreCase(format) || "PGM FILE".equalsIgnoreCase(format);
    }

    @Override
    public boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {
        int[][] convertedPixels;

        // Convert the image pixels depending on the current format of the image
        if ("PBM".equalsIgnoreCase(image.getFormat()) || "PBM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P1 to P2
            convertedPixels = ImageConversionUtils.convertP1ToP2(image.getPixels(), image.getWidth(), image.getHeight());
        } else if ("PPM".equalsIgnoreCase(image.getFormat()) || "PPM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P3 to P2
            convertedPixels = ImageConversionUtils.convertP3ToP2(image.getPixels(), image.getWidth(), image.getHeight());
        } else {
            // Already in P2 format, no conversion needed
            convertedPixels = image.getPixels();
        }

        Image convertedImage = new Image(image.getWidth(), image.getHeight(), convertedPixels, targetFormat, file);

        // Use the existing ImageSaverChain to save the converted image
        return new ImageSaverChain().saveImage(convertedImage, file);
    }
}

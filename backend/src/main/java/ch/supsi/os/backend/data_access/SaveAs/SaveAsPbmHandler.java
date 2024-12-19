package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.data_access.Save.ImageSaverChain;
import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class SaveAsPbmHandler extends SaveAsHandler {
    @Override
    protected boolean canHandle(String format) {
        return  "PBM".equalsIgnoreCase(format) || "PBM FILE".equalsIgnoreCase(format);
    }

    @Override
    public boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {

        int[][] convertedPixels;

        // Convert the image pixels depending on the current format of the image
        if ("PGM".equalsIgnoreCase(image.getFormat()) || "PGM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P2 to P1
            convertedPixels = ImageConversionUtils.convertP2ToP1(image.getPixels(), image.getWidth(), image.getHeight());
        } else if ("PPM".equalsIgnoreCase(image.getFormat()) || "PPM FILE".equalsIgnoreCase(image.getFormat())) {
            // Convert from P3 to P1
            convertedPixels = ImageConversionUtils.convertP3ToP1(image.getPixels(), image.getWidth(), image.getHeight());
        } else {
            // Already in P1 format, no conversion needed
            convertedPixels = image.getPixels();
        }

        Image convertedImage = new Image(image.getWidth(), image.getHeight(), convertedPixels, targetFormat, file);

        // Use the existing ImageSaverChain to save the converted image
        return new ImageSaverChain().saveImage(convertedImage, file);
    }
}

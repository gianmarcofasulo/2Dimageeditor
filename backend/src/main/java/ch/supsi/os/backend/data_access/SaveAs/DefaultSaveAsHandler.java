package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class DefaultSaveAsHandler extends SaveAsHandler {

    @Override
    public boolean canHandle(String format) {
        return false;
    }

    @Override
    public boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {
        throw new IllegalArgumentException("Unsupported target format: " + targetFormat);
    }
}

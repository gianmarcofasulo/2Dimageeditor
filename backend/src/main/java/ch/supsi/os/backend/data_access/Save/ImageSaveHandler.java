package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import java.io.File;
import java.io.IOException;

public interface ImageSaveHandler {
    void setNextHandler(ImageSaveHandler handler);
    boolean save(Image image, File file) throws IOException;
}

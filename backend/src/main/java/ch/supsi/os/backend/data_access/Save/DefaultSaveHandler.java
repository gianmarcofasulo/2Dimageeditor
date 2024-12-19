package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import java.io.File;

public class DefaultSaveHandler extends BaseImageSaveHandler {
    @Override
    public boolean save(Image image, File file) {
        return false;
    }
}

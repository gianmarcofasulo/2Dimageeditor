package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import java.io.File;
import java.io.IOException;

public abstract class BaseImageSaveHandler implements ImageSaveHandler {
    protected ImageSaveHandler nextHandler;

    @Override
    public void setNextHandler(ImageSaveHandler handler) {
        this.nextHandler = handler;
    }

    protected boolean saveNext(Image image, File file) throws IOException {
        return nextHandler != null && nextHandler.save(image, file);
    }
}

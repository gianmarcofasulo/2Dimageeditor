package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public abstract class SaveAsHandler {
    private SaveAsHandler nextHandler;

    public void setNextHandler(SaveAsHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean saveAs(Image image, File file, String targetFormat) throws IOException {
        if (canHandle(targetFormat)) {
            return handleSaveAs(image, file, targetFormat);
        } else if (nextHandler != null) {
            return nextHandler.saveAs(image, file, targetFormat);
        }
        throw new IllegalArgumentException("Unsupported target format: " + targetFormat);
    }

    protected abstract boolean canHandle(String format);

    protected abstract boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException;
}

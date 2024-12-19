package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public interface ImageLoader {
    Image load(File file) throws IOException;
}

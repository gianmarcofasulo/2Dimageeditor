package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.exception.FormatException;
import ch.supsi.os.backend.model.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class DefaultLoadHandler extends BaseImageLoadHandler {

    @Override
    protected Image handleLoad(File file) throws IOException, FormatException {
        // Questo handler non carica nessun formato specifico
        BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
        throw new FormatException(reader.readLine());
    }
}

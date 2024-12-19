package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import java.io.File;
import java.io.IOException;

public class ImageSaverChain {
    private final ImageSaveHandler chain;

    public ImageSaverChain() {
        ImageSaveHandler pgmHandler = new PgmSaveHandler();
        ImageSaveHandler ppmHandler = new PpmSaveHandler();
        ImageSaveHandler pbmHandler = new PbmSaveHandler();
        ImageSaveHandler defaultHandler = new DefaultSaveHandler();

        // Configura la catena: PGM -> PPM -> PBM -> Default
        pgmHandler.setNextHandler(ppmHandler);
        ppmHandler.setNextHandler(pbmHandler);
        pbmHandler.setNextHandler(defaultHandler);

        this.chain = pgmHandler;
    }

    public boolean saveImage(Image image, File file) throws IOException {
        return chain.save(image, file);
    }
}


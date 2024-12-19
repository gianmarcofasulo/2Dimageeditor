package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public class SaveAsChain {
    private final SaveAsHandler chain;

    public SaveAsChain() {
        // Create and link the chain
        SaveAsHandler pbmHandler = new SaveAsPbmHandler();
        SaveAsHandler pgmHandler = new SaveAsPgmHandler();
        SaveAsHandler ppmHandler = new SaveAsPpmHandler();
        SaveAsHandler defaultHandler = new DefaultSaveAsHandler();

        pgmHandler.setNextHandler(ppmHandler);
        ppmHandler.setNextHandler(pbmHandler);
        pbmHandler.setNextHandler(defaultHandler);

        this.chain = pgmHandler;
    }

    // Constructor for testing with a custom chain
    public SaveAsChain(SaveAsHandler customChain) {
        this.chain = customChain;
    }

    public boolean saveAs(Image image, File file, String targetFormat) throws IOException {
        String normalizedFormat = targetFormat.trim().toUpperCase();
        return chain.saveAs(image, file, normalizedFormat);
    }
}

package ch.supsi.os.backend.data_access.Loader;

public class ImageLoaderChain {

    private final BaseImageLoadHandler handlerChain;

    public ImageLoaderChain() {
        handlerChain = new PbmLoadHandler();
        BaseImageLoadHandler pgmHandler = new PgmLoadHandler();
        BaseImageLoadHandler ppmHandler = new PpmLoadHandler();
        BaseImageLoadHandler defaultHandler = new DefaultLoadHandler();

        handlerChain.setNextHandler(pgmHandler);
        pgmHandler.setNextHandler(ppmHandler);
        ppmHandler.setNextHandler(defaultHandler); // Aggiunge il gestore di default
    }

    public BaseImageLoadHandler getHandlerChain() {
        return handlerChain;
    }
}

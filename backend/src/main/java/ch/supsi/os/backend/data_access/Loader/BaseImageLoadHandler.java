package ch.supsi.os.backend.data_access.Loader;

import ch.supsi.os.backend.exception.FormatException;
import ch.supsi.os.backend.model.Image;

import java.io.File;
import java.io.IOException;

public abstract class BaseImageLoadHandler {

    private BaseImageLoadHandler nextHandler;

    /**
     * Imposta il prossimo handler nella catena.
     *
     * @param nextHandler il prossimo handler
     */
    public void setNextHandler(BaseImageLoadHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Gestisce il caricamento di un'immagine.
     *
     * @param file il file da caricare
     * @return l'immagine caricata
     * @throws IOException se nessun handler può gestire il file
     */
    public Image loadImage(File file) throws IOException, FormatException {
        // Prova a caricare l'immagine con il gestore corrente
        if(file.length() == 0){
            throw new NullPointerException("");
        }
        Image image = handleLoad(file);
        if (image != null) {
            return image;
        }
        // Passa al successivo nella catena
        if (nextHandler != null) {
            return nextHandler.loadImage(file);
        }
        // Se nessun gestore può caricarlo, lancia un'eccezione
        throw new FormatException("");
    }

    /**
     * Metodo astratto per gestire il caricamento di un'immagine.
     *
     * @param file il file da caricare
     * @return l'immagine caricata o null se il formato non è supportato
     * @throws IOException se si verifica un errore
     */
    protected abstract Image handleLoad(File file) throws IOException, FormatException;
}

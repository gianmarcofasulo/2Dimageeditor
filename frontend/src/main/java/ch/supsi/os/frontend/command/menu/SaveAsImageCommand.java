package ch.supsi.os.frontend.command.menu;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.frontend.controller.ImageController;
import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.model.PreferencesModel;
import ch.supsi.os.frontend.view.AlertPopup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class SaveAsImageCommand implements MenuCommand {

    private final ImageController imageController;
    private final EventDispatcher eventDispatcher;
    private final ResourceBundle resources;
    private final PreferencesModel preferencesModel;

    public SaveAsImageCommand(ImageController imageController, EventDispatcher eventDispatcher, ResourceBundle resources) {
        this.imageController = imageController;
        this.eventDispatcher = eventDispatcher;
        this.resources = resources;
        this.preferencesModel = PreferencesModel.getInstance(); // Inizializza il modello delle preferenze
    }

    @Override
    public void execute() {
        Image image = imageController.getCurrentImage();

        if (image == null) {
            // Mostra l'errore quando non è presente alcuna immagine
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.no_image");
            String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.no_image_message");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);
            return;
        }

        // Configura la finestra di dialogo per selezionare un file di destinazione
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("ui.menuitem.saveas"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PBM file", "*.pbm"),
                new FileChooser.ExtensionFilter("PGM file", "*.pgm"),
                new FileChooser.ExtensionFilter("PPM file", "*.ppm")
        );

        // Imposta la directory iniziale dall'ultima cartella aperta
        Path lastOpenedFolder = preferencesModel.getLastOpenedFolder();
        if (lastOpenedFolder != null && lastOpenedFolder.toFile().exists()) {
            fileChooser.setInitialDirectory(lastOpenedFolder.toFile());
        }

        File newFile = fileChooser.showSaveDialog(new Stage());
        if (newFile == null) {
            // Mostra l'errore in caso di annullamento
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = resources.getString("ui.status.fileOperation");
            String errorMessage = resources.getString("ui.status.fileOpenFailedNull");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);
            return;
        }

        try {
            // Salva l'immagine nel nuovo file di destinazione
            imageController.saveAsImage(image, newFile, fileChooser.getSelectedExtensionFilter().getDescription());

            // Aggiorna l'ultima cartella aperta
            preferencesModel.setLastOpenedFolder(newFile.toPath().getParent());
            preferencesModel.save();

            // Dispatch event di successo
            String message = String.format(resources.getString("ui.status.fileSavedAs"), newFile.getName());
            eventDispatcher.dispatch(AppEventType.FILE_SAVED_AS, message);

        } catch (Exception e) {
            // Gestisci errore di salvataggio
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = resources.getString("ui.alert.error.file_error");
            String errorMessage = LocalizationService.getLocalizedString("user.message.save.error");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);

            String message = resources.getString("ui.status.fileOpenFailed");
            eventDispatcher.dispatch(AppEventType.FILE_SAVE_FAILED, message);
        }
    }
}

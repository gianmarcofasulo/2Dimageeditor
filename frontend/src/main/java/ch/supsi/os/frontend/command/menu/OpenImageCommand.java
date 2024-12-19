package ch.supsi.os.frontend.command.menu;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.backend.exception.FormatException;
import ch.supsi.os.backend.exception.NegativeException;
import ch.supsi.os.frontend.controller.ImageController;
import ch.supsi.os.backend.business.ImageService;
import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.model.PreferencesModel;
import ch.supsi.os.frontend.view.AlertPopup;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class OpenImageCommand implements MenuCommand {

    private final ImageService imageService;
    private final ImageController imageController;
    private final PreferencesModel preferencesModel;
    private final EventDispatcher eventDispatcher;
    private final ResourceBundle resources;

    // Constructor to pass ImageController instance
    public OpenImageCommand(ImageController imageController, EventDispatcher eventDispatcher, ResourceBundle resources) {
        this.imageService = new ImageService();
        this.imageController = imageController;
        this.preferencesModel = PreferencesModel.getInstance();
        this.eventDispatcher = eventDispatcher;
        this.resources = resources;
    }

    @Override
    public void execute() {
        // Create a file chooser supporting PBM, PGM, and PPM formats
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Image Files", "*.pbm", "*.pgm", "*.ppm"),
                new FileChooser.ExtensionFilter("PBM Files (P1)", "*.pbm"),
                new FileChooser.ExtensionFilter("PGM Files (P2)", "*.pgm"),
                new FileChooser.ExtensionFilter("PPM Files (P3)", "*.ppm")
        );

        // Set initial directory from preferences
        Path lastOpenedFolder = preferencesModel.getLastOpenedFolder();
        if (lastOpenedFolder != null && lastOpenedFolder.toFile().exists()) {
            fileChooser.setInitialDirectory(lastOpenedFolder.toFile());
        }

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String fileName = file.getName();
            Image image = null;
            try {
                // Carica l'immagine utilizzando il servizio
                image = imageService.loadImage(file);

                imageService.validateImage(image);

                imageController.drawImageOnCanvas(image);
                preferencesModel.setLastOpenedFolder(file.toPath().getParent());
                preferencesModel.save();

                // Dispatch success event with file name
                String message = String.format(resources.getString("ui.status.fileOpened"), image.getFile().getName());
                eventDispatcher.dispatch(AppEventType.FILE_OPENED, message);

            }catch (NegativeException e) {
                // Negative values in image
                String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
                String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.negative_values");
                String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.negative_values_message");

                String message = resources.getString("ui.status.fileOpenFailedNegative");
                eventDispatcher.dispatch(AppEventType.FILE_OPEN_FAILED, message);

                AlertPopup.showError(errorTitle, errorHeader, errorMessage);
            } catch (NullPointerException e){
                String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
                String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.file_error");
                String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.empty");

                String message = resources.getString("ui.status.fileOpenFailedNull");
                eventDispatcher.dispatch(AppEventType.FILE_OPEN_FAILED, message);

                AlertPopup.showError(errorTitle, errorHeader, errorMessage);
            } catch (FormatException e) {
                // Unsupported file format
                String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
                String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.unsupported_format");
                String errorMessage = String.format(
                        "%s: %s",
                        LocalizationService.getLocalizedString("ui.alert.error.unsupported_format_message"),
                        e.getMessage()
                );

                String message = String.format(resources.getString("ui.status.fileOpenFailedFormat"), e.getMessage());
                eventDispatcher.dispatch(AppEventType.FILE_OPEN_FAILED, message);

                AlertPopup.showError(errorTitle, errorHeader, errorMessage);

            } catch (Exception e) {
                String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
                String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.file_error");
                String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.corrupted");

                String message = resources.getString("ui.status.fileOpenFailedCorrupted");
                eventDispatcher.dispatch(AppEventType.FILE_OPEN_FAILED, message);

                AlertPopup.showError(errorTitle, errorHeader, errorMessage);
            }
        }
    }


}
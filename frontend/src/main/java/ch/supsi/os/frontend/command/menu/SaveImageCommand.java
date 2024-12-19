package ch.supsi.os.frontend.command.menu;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.frontend.controller.ImageController;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.view.AlertPopup;
import ch.supsi.os.backend.business.LocalizationService;

import java.io.File;
import java.util.ResourceBundle;

public class SaveImageCommand implements MenuCommand {

    private final ImageController imageController;
    private final EventDispatcher eventDispatcher;
    private final ResourceBundle resources;

    public SaveImageCommand(ImageController imageController, EventDispatcher eventDispatcher, ResourceBundle resources) {
        this.imageController = imageController;
        this.eventDispatcher = eventDispatcher;
        this.resources = resources;
    }

    @Override
    public void execute() {
        Image image = imageController.getCurrentImage();

        if (image == null) {
            // Show error if no image is loaded
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.no_image");
            String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.no_image_message");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);

            // Dispatch a failed event
            eventDispatcher.dispatch(AppEventType.FILE_SAVE_FAILED, resources.getString("ui.status.fileOpenFailedNull"));
            return;
        }

        File file = image.getFile();
        if (file == null) {
            // Show error if there's no source file
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = LocalizationService.getLocalizedString("ui.alert.error.file_error");
            String errorMessage = LocalizationService.getLocalizedString("ui.alert.error.no_file_message");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);

            // Dispatch a failed event
            eventDispatcher.dispatch(AppEventType.FILE_SAVE_FAILED, resources.getString("ui.status.fileSaveFailed"));
            return;
        }

        try {
            // Save the image to its current file
            imageController.saveImage(image, file);

            // Dispatch a success event
            String message = String.format(resources.getString("ui.status.fileSaved"), file.getName());
            eventDispatcher.dispatch(AppEventType.FILE_SAVED, message);

        } catch (Exception e) {
            // Handle saving error
            String errorTitle = LocalizationService.getLocalizedString("ui.alert.error.title");
            String errorHeader = resources.getString("ui.alert.error.file_error");
            String errorMessage = LocalizationService.getLocalizedString("user.message.save.error");
            AlertPopup.showError(errorTitle, errorHeader, errorMessage);

            // Dispatch a failed event
            String message = resources.getString("ui.status.fileSaveFailed");
            eventDispatcher.dispatch(AppEventType.FILE_SAVE_FAILED, message);
        }
    }
}

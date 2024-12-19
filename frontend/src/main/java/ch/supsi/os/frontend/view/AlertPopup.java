package ch.supsi.os.frontend.view;

import ch.supsi.os.backend.business.LocalizationService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertPopup {

    /**
     * Displays an error popup with the specified message.
     * If title, header, or content are null or empty, localized defaults are used.
     *
     * @param title The title of the dialog.
     * @param header The header text of the error message.
     * @param content The content text of the error message.
     */
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);

        // Use localized title if "title" is null or empty
        alert.setTitle((title == null || title.isEmpty())
                ? LocalizationService.getLocalizedString("ui.alert.error.title")
                : title);

        // Use localized header if "header" is null or empty
        alert.setHeaderText((header == null || header.isEmpty())
                ? LocalizationService.getLocalizedString("ui.alert.error.header")
                : header);

        // Use localized content if "content" is null or empty
        alert.setContentText((content == null || content.isEmpty())
                ? LocalizationService.getLocalizedString("ui.alert.error.content")
                : content);

        alert.showAndWait();
    }
}

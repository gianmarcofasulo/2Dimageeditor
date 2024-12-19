package ch.supsi.os.frontend.view;

import ch.supsi.os.backend.business.LocalizationService;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class ConfirmationPopup {

    private final String header;
    private final String title;
    private final String content;

    public ConfirmationPopup(final String headerKey, final String titleKey, final String contentKey) {
        // Retrieve localized strings based on the current language at startup
        this.header = LocalizationService.getLocalizedString(headerKey);
        this.title = LocalizationService.getLocalizedString(titleKey);
        this.content = LocalizationService.getLocalizedString(contentKey);
    }

    public boolean show() {
        // Create confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Set focus on cancel button
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDefaultButton(true);
        cancelButton.setDefaultButton(false);

        // Set the alert properties with the localized strings
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }
}

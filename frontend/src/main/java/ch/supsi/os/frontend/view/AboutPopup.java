package ch.supsi.os.frontend.view;

import ch.supsi.os.frontend.model.PropertiesHandler;
import ch.supsi.os.backend.business.LocalizationService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AboutPopup {

    private final PropertiesHandler propertiesHandler;

    public AboutPopup(PropertiesHandler propertiesModel) {
        this.propertiesHandler = propertiesModel;
    }

    public void show() {
        // Retrieve localized strings each time the popup is shown
        String title = LocalizationService.getLocalizedString("ui.menuitem.about");
        String header = LocalizationService.getLocalizedString("user.message.welcome");
        String closeButtonLabel = LocalizationService.getLocalizedString("ui.button.close");

        // Create a custom close button with localized text
        ButtonType closeButton = new ButtonType(closeButtonLabel);

        Alert alert = new Alert(Alert.AlertType.NONE, propertiesHandler.getDescription() + "\n"
                + propertiesHandler.getVersion(), closeButton);

        // Set title and header directly with localized text
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();

        if (alert.getResult() == closeButton) {
            alert.close();
        }
    }
}

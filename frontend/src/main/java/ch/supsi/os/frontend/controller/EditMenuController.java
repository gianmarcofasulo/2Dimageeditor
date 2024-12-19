package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.frontend.model.PreferencesHandler;
import ch.supsi.os.backend.business.LocalizationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditMenuController implements Initializable {
    private final PreferencesHandler preferencesHandler;
    private final ResourceBundle resources;
    private final EventDispatcher eventDispatcher; // Add EventDispatcher

    @FXML
    private Menu miLanguage;

    public EditMenuController(PreferencesHandler preferencesHandler, ResourceBundle resources, EventDispatcher eventDispatcher) {
        this.preferencesHandler = preferencesHandler;
        this.resources = resources;
        this.eventDispatcher = eventDispatcher; // Initialize EventDispatcher
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        miLanguage.setText(LocalizationService.getLocalizedString("ui.menuitem.language"));
        buildLanguageMenu();
    }

    private void buildLanguageMenu() {
        Objects.requireNonNull(LocalizationService.getLocales()).forEach((locale) -> {
            MenuItem mi = new MenuItem(locale.getDisplayLanguage(locale));
            mi.setOnAction((event) -> {
                // Save the selected locale to preferences
                LocalizationService.setLocale(locale);
                preferencesHandler.setLocale(locale);

                // Show restart notification with the selected language
                showRestartNotification(locale);
            });
            miLanguage.getItems().add(mi);
        });
    }

    private void showRestartNotification(Locale newLocale) {
        // Get the language name in the new locale
        String languageName = newLocale.getDisplayLanguage(newLocale);

        // Format the message to include the new language
        String message = String.format(resources.getString("ui.notification.restartRequiredWithLanguage"), languageName);

        // Dispatch the event with the formatted message
        eventDispatcher.dispatch(AppEventType.LANGUAGE_CHANGED, message);
    }
}

package ch.supsi.os.frontend.controller;

import ch.supsi.os.frontend.command.menu.ShowAboutMenuCommand;
import ch.supsi.os.frontend.model.PropertiesHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpMenuController implements Initializable {
    private final ShowAboutMenuCommand showAboutMenuCommand;
    private final ResourceBundle resources;

    @FXML
    private MenuItem miAbout;

    public HelpMenuController(PropertiesHandler propertiesHandler, ResourceBundle resources) {
        this.showAboutMenuCommand = new ShowAboutMenuCommand(propertiesHandler);
        this.resources = resources;

    }

    @FXML
    public void showAbout(ActionEvent e) {
        showAboutMenuCommand.execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set static text for 'About' menu item using the ResourceBundle
        miAbout.setText(resources.getString("ui.menuitem.about"));

        // Set F1 as the accelerator key (shortcut) for the About menu item
        miAbout.setAccelerator(new KeyCodeCombination(javafx.scene.input.KeyCode.F1));
    }
}

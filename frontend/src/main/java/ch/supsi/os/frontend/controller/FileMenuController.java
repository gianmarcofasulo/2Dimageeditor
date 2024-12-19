package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.frontend.command.menu.OpenImageCommand;
import ch.supsi.os.frontend.command.menu.QuitCommand;
import ch.supsi.os.frontend.command.menu.SaveAsImageCommand;
import ch.supsi.os.frontend.command.menu.SaveImageCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FileMenuController implements Initializable {

    private @FXML MenuItem miNew;
    private @FXML MenuItem miOpen;
    private @FXML MenuItem miSave;
    private @FXML MenuItem miSaveAs;
    private @FXML MenuItem miQuit;

    private final ImageController imageController;
    private final ResourceBundle resources;
    private final EventDispatcher eventDispatcher; // Add EventDispatcher

    private Stage stage;

    public FileMenuController(ImageController imageController, ResourceBundle resources, EventDispatcher eventDispatcher) {
        this.imageController = imageController;
        this.resources = resources;
        this.eventDispatcher = eventDispatcher; // Initialize EventDispatcher
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void openImage(ActionEvent e) {
        new OpenImageCommand(imageController, eventDispatcher, resources).execute();
    }

    @FXML
    public void saveImage(ActionEvent e) {
        new SaveImageCommand(imageController, eventDispatcher, resources).execute();
    }

    @FXML
    public void saveImageAs(ActionEvent e) {
        new SaveAsImageCommand(imageController,eventDispatcher, resources).execute();
    }

    @FXML
    public void quit(ActionEvent e) {
        new QuitCommand(stage).execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set static text for menu items using the ResourceBundle
        miOpen.setText(resources.getString("ui.menuitem.open"));
        miSave.setText(resources.getString("ui.menuitem.save"));
        miSaveAs.setText(resources.getString("ui.menuitem.saveas"));
        miQuit.setText(resources.getString("ui.menuitem.quit"));

        miOpen.setAccelerator(new KeyCodeCombination(
                javafx.scene.input.KeyCode.O,
                KeyCombination.SHORTCUT_DOWN
        ));
        miSave.setAccelerator(new KeyCodeCombination(
                javafx.scene.input.KeyCode.S,
                KeyCombination.SHORTCUT_DOWN
        ));
        miSaveAs.setAccelerator(new KeyCodeCombination(
                javafx.scene.input.KeyCode.S,
                KeyCombination.SHORTCUT_DOWN,
                KeyCombination.SHIFT_DOWN
        ));
    }
}

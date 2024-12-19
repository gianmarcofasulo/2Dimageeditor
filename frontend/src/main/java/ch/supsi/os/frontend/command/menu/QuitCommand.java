package ch.supsi.os.frontend.command.menu;

import ch.supsi.os.frontend.MainFx;
import ch.supsi.os.frontend.view.ConfirmationPopup;
import javafx.application.Platform;
import javafx.stage.Stage;

public class QuitCommand implements MenuCommand {

    private final ConfirmationPopup confirmationDialog;
    private final Stage stage;

    public QuitCommand(Stage stage) {
        this.confirmationDialog = new ConfirmationPopup("ui.alert.quit.header", "ui.alert.quit.title", "ui.alert.quit.content");
        this.stage = stage;
    }

    @Override
    public void execute() {
        if (confirmationDialog.show()) {
            MainFx.savePreferences(stage);
            Platform.exit();
        }
    }
}

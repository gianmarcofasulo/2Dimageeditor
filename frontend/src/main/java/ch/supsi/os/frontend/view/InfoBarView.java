package ch.supsi.os.frontend.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class InfoBarView {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox messageBox;

    // Store messages as static strings
    private final List<String> messages = new ArrayList<>();
    private boolean welcomeMessageDisplayed = false;  // Flag for welcome message

    public void initialize() {
        // Add listener to scroll to the bottom whenever a new message is added
        messageBox.heightProperty().addListener((observable, oldValue, newValue) -> scrollToBottom());
    }

    public void addWelcomeMessage(String welcomeMessage) {
        // Add welcome message and set flag
        Text messageText = new Text(welcomeMessage);
        messageText.setStyle("-fx-fill: black;");
        messageBox.getChildren().add(messageText);
        messages.add(welcomeMessage);
        welcomeMessageDisplayed = true;  // Set flag to indicate welcome message is present

        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    public void addMessage(String message) {
        if (welcomeMessageDisplayed) {
            // Remove welcome message if it exists
            messageBox.getChildren().remove(0);
            messages.remove(0);
            welcomeMessageDisplayed = false;
        }

        Text messageText = new Text(message);
        messageText.setStyle("-fx-fill: black;");
        messageBox.getChildren().add(messageText);
        messages.add(message);

        Platform.runLater(() -> scrollPane.setVvalue(1.0));

        // Limit to the last 5 messages
        if (messageBox.getChildren().size() > 5) {
            messages.remove(0);
            messageBox.getChildren().remove(0);
        }
    }

    private void scrollToBottom() {
        // Use Platform.runLater to ensure it executes after layout updates
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}

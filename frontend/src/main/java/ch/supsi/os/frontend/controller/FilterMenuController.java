package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.frontend.command.pipeline.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FilterMenuController implements Initializable {

    private final ImageController imageController;
    private final EventDispatcher eventDispatcher;
    private PipelineController pipeline;
    private List<PipelineCommand> pendingCommands = new ArrayList<>();
    private final ResourceBundle resources;

    @FXML
    private Button leftTopButton;
    @FXML
    private Button leftTopButton1;
    @FXML
    private Button leftTopButton2;
    @FXML
    private Button leftTopButton3;
    @FXML
    private Button leftBottomButton;
    @FXML
    private Label labelFilterSection;
    @FXML
    private Label labelEditSection;

    // Store static messages for events (translated at initialization)
    private String rotateRightMessage;
    private String rotateLeftMessage;
    private String flipHorizontallyMessage;
    private String flipVerticallyMessage;
    private String negativeFilterMessage;


    // Constructor to initialize the image controller and event dispatcher
    public FilterMenuController(ImageController imageController, EventDispatcher eventDispatcher, ResourceBundle resources) {
        this.imageController = imageController;
        this.eventDispatcher = eventDispatcher;
        this.imageController.setFilterMenuController(this);
        this.pipeline = PipelineController.getInstance();
        this.resources = resources;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set static text for UI elements based on the ResourceBundle
        labelFilterSection.setText(resourceBundle.getString("ui.label.filter.section"));
        labelEditSection.setText(resourceBundle.getString("ui.label.edit.section"));

        // Pre-load static messages for event dispatching
        rotateRightMessage = resourceBundle.getString("ui.status.transformationApplied.rotateRight");
        rotateLeftMessage = resourceBundle.getString("ui.status.transformationApplied.rotateLeft");
        flipHorizontallyMessage = resourceBundle.getString("ui.status.transformationApplied.flipHorizontally");
        flipVerticallyMessage = resourceBundle.getString("ui.status.transformationApplied.flipVertically");
        negativeFilterMessage = resourceBundle.getString("ui.status.filterApplied.negative");
    }

    @FXML
    public void rotateRight() {
        pipeline.addCommand(new RotateRightCommand());
        eventDispatcher.dispatch(AppEventType.TRANSFORMATION_APPLIED, rotateRightMessage);
    }

    @FXML
    public void rotateLeft() {
        pipeline.addCommand(new RotateLeftCommand());
        eventDispatcher.dispatch(AppEventType.TRANSFORMATION_APPLIED, rotateLeftMessage);
    }

    @FXML
    public void flipHorizontally() {
        pipeline.addCommand(new FlipHorizontallyCommand());
        eventDispatcher.dispatch(AppEventType.TRANSFORMATION_APPLIED, flipHorizontallyMessage);
    }

    @FXML
    public void flipVertically() {
        pipeline.addCommand(new FlipVerticallyCommand());
        eventDispatcher.dispatch(AppEventType.TRANSFORMATION_APPLIED, flipVerticallyMessage);
    }

    @FXML
    public void negative() {
        pipeline.addCommand(new NegativeCommand());
        eventDispatcher.dispatch(AppEventType.FILTER_APPLIED, negativeFilterMessage);
    }

    // Method to enable buttons
    public void enableButtons() {
        leftTopButton.setDisable(false);
        leftTopButton1.setDisable(false);
        leftTopButton2.setDisable(false);
        leftTopButton3.setDisable(false);
        leftBottomButton.setDisable(false);
    }

    public void setPipelineController(PipelineController pipelineController) {
        this.pipeline = pipelineController;

        // Add any pending commands to the pipeline controller
        for (PipelineCommand command : pendingCommands) {
            pipelineController.addCommand(command);
        }
        pendingCommands.clear();  // Clear after adding to avoid duplication
    }
}

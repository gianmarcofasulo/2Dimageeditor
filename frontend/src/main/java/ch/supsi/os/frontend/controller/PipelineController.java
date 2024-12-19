package ch.supsi.os.frontend.controller;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.backend.model.Image;
import ch.supsi.os.frontend.command.pipeline.PipelineCommand;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PipelineController {

    @FXML
    private VBox actionList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button playButton;

    private static volatile PipelineController instance;
    private ImageController imageController;
    private EventDispatcher eventDispatcher;
    private ResourceBundle resources;
    private final List<PipelineCommand> commands = new ArrayList<>();
    private BooleanBinding isPipelineEmpty;
    private int draggedIndex = -1;

    // Private constructor to prevent instantiation
    private PipelineController() {
    }

    // Singleton getInstance method with lazy initialization
    public static PipelineController getInstance() {
        if (instance == null) {
            synchronized (PipelineController.class) {
                if (instance == null) {
                    instance = new PipelineController();
                }
            }
        }
        return instance;
    }

    // Method to initialize ImageController and other dependencies
    public void initializeDependencies(ImageController imageController, EventDispatcher eventDispatcher, ResourceBundle resources) {
        this.imageController = imageController;
        this.eventDispatcher = eventDispatcher;
        this.resources = resources;
    }

    @FXML
    public void initialize() {
        if (actionList == null) {
            System.out.println("Error: actionList is null! Check FXML binding.");
        } else {
            System.out.println("actionList initialized successfully.");
        }

        isPipelineEmpty = new BooleanBinding() {
            {
                super.bind(actionList.getChildren());
            }

            @Override
            protected boolean computeValue() {
                return actionList.getChildren().isEmpty();
            }
        };

        deleteButton.disableProperty().bind(isPipelineEmpty);
        playButton.disableProperty().bind(isPipelineEmpty);

        deleteButton.setOnAction(event -> clearPipeline());
        playButton.setOnAction(event -> executePipelineWithUIFeedback());
    }

    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }

    public void addCommand(PipelineCommand command) {
        commands.add(command);

        String description = command.getDescription();
        Label stepLabel = new Label(description);
        stepLabel.setMaxWidth(Double.MAX_VALUE);
        stepLabel.getStyleClass().add("step-label");

        setUpDragEvents(stepLabel);
        actionList.getChildren().add(stepLabel);
    }

    private void setUpDragEvents(Label stepLabel) {
        stepLabel.setOnDragDetected(event -> {
            draggedIndex = actionList.getChildren().indexOf(stepLabel);

            if (draggedIndex != -1) {
                Dragboard dragboard = stepLabel.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(stepLabel.getText());
                dragboard.setContent(content);
                event.consume();
            }
        });

        stepLabel.setOnDragOver(event -> {
            if (event.getGestureSource() != stepLabel && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        stepLabel.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                int targetIndex = actionList.getChildren().indexOf(stepLabel);

                if (draggedIndex != -1 && targetIndex != -1 && draggedIndex != targetIndex) {
                    reorderCommands(draggedIndex, targetIndex);
                    reorderActionList(draggedIndex, targetIndex);

                    if (eventDispatcher != null && resources != null) {
                        eventDispatcher.dispatch(
                                AppEventType.PIPELINE,
                                resources.getString("ui.status.pipeline.orderChanged")
                        );
                    }
                }

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void reorderCommands(int fromIndex, int toIndex) {
        if (fromIndex < toIndex) {
            commands.add(toIndex, commands.get(fromIndex));
            commands.remove(fromIndex);
        } else {
            PipelineCommand command = commands.remove(fromIndex);
            commands.add(toIndex, command);
        }
    }

    private void reorderActionList(int fromIndex, int toIndex) {
        Node label = actionList.getChildren().remove(fromIndex);
        actionList.getChildren().add(toIndex, label);
    }

    public void executePipeline(Image initialImage) {
        for (PipelineCommand command : commands) {
            command.execute(initialImage);
        }
    }

    public void clearPipeline() {
        commands.clear();
        actionList.getChildren().clear();

        if (eventDispatcher != null && resources != null) {
            eventDispatcher.dispatch(
                    AppEventType.PIPELINE,
                    resources.getString("ui.status.pipeline.cleared")
            );
        }
    }

    @FXML
    private void executePipelineWithUIFeedback() {
        Image currentImage = imageController.getCurrentImage();

        if (currentImage != null) {
            executePipeline(currentImage);
            imageController.drawImageOnCanvas(currentImage);

            if (eventDispatcher != null && resources != null) {
                eventDispatcher.dispatch(
                        AppEventType.PIPELINE,
                        resources.getString("ui.status.pipeline.executionStarted")
                );
            }
        } else {
            System.out.println("No image loaded, please load an image first.");
        }
    }
}

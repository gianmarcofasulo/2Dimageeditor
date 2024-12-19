package ch.supsi.os.frontend;

import ch.supsi.os.backend.business.EventDispatcher;
import ch.supsi.os.frontend.controller.*;
import ch.supsi.os.backend.model.LocalizationModel;
import ch.supsi.os.frontend.model.PreferencesModel;
import ch.supsi.os.frontend.model.PropertiesModel;
import ch.supsi.os.frontend.controller.FilterMenuController;
import ch.supsi.os.frontend.controller.ImageController;
import ch.supsi.os.backend.business.LocalizationService;
import ch.supsi.os.frontend.view.InfoBarView;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.stage.WindowEvent;

public class MainFx extends Application {
    private static final PreferencesModel preferencesModel = PreferencesModel.getInstance();
    private final EventDispatcher eventDispatcher = new EventDispatcher();

    public MainFx() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load ResourceBundle based on current locale
        Locale preferredLocale = preferencesModel.getLocale();
        LocalizationModel localizationModel = LocalizationModel.getInstance();
        localizationModel.init("i18n.translations", preferredLocale);
        if (!localizationModel.isInitialized()) {
            localizationModel.init("i18n.translations", Locale.US);
        }
        LocalizationService.init(localizationModel);
        ResourceBundle resources = ResourceBundle.getBundle("i18n.translations", LocalizationModel.getInstance().getLocale());

        // Load Edit Menu FXML
        URL editMenuFxmlUrl = getClass().getResource("/preferencesmenu.fxml");
        if (editMenuFxmlUrl == null) {
            System.out.println("preferencesmenu.fxml not found");
            return;
        }

        // Load Help Menu FXML
        URL helpMenuFxmlUrl = getClass().getResource("/helpmenu.fxml");
        if (helpMenuFxmlUrl == null) {
            return;
        }
        FXMLLoader helpMenuLoader = new FXMLLoader(helpMenuFxmlUrl, resources);
        helpMenuLoader.setControllerFactory(c -> new HelpMenuController(PropertiesModel.getInstance(), resources));
        Menu helpMenu = helpMenuLoader.load();
        helpMenu.setText(resources.getString("ui.menu.help"));

        // Load Preferences FXML
        URL preferencesFxmlUrl = getClass().getResource("/preferencesmenu.fxml");
        if (preferencesFxmlUrl == null) {
            System.out.println("preferencesmenu.fxml not found");
            return;
        }
        FXMLLoader editMenuLoader = new FXMLLoader(preferencesFxmlUrl, resources);
        editMenuLoader.setControllerFactory(c -> new EditMenuController(preferencesModel, resources, eventDispatcher));
        Menu preferencesMenu = editMenuLoader.load();
        preferencesMenu.setText(resources.getString("ui.menu.edit"));

        // Load File Menu FXML
        URL fileMenuFxmlUrl = getClass().getResource("/filemenu.fxml");
        if (fileMenuFxmlUrl == null) {
            System.out.println("filemenu.fxml not found");
            return;
        }

        FXMLLoader imageViewLoader = new FXMLLoader(getClass().getResource("/imageview.fxml"));
        Parent root = imageViewLoader.load();
        ImageController controller = imageViewLoader.getController();

        FXMLLoader fileMenuLoader = new FXMLLoader(fileMenuFxmlUrl, resources);
        fileMenuLoader.setControllerFactory(c -> new FileMenuController(controller, resources, eventDispatcher));
        Menu fileMenu = fileMenuLoader.load();
        fileMenu.setText(resources.getString("ui.menu.file"));

        // Get the FileMenuController instance and set the Stage
        FileMenuController fileMenuController = fileMenuLoader.getController();
        fileMenuController.setStage(stage);

        // Load Info Bar FXML
        URL infoBarFxmlUrl = getClass().getResource("/infobar.fxml");
        if (infoBarFxmlUrl == null) {
            System.out.println("infobar.fxml not found");
            return;
        }
        FXMLLoader infoBarLoader = new FXMLLoader(infoBarFxmlUrl, resources);
        InfoBarView infoBarView = new InfoBarView();
        infoBarLoader.setController(infoBarView);
        Parent infoBar = infoBarLoader.load();

        StatusBarController statusBarController = new StatusBarController(eventDispatcher, infoBarView, resources);

        // Create MenuBar and add Menus
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, preferencesMenu, helpMenu);

        // Load Left Section (Filter Menu) from filtermenu.fxml
        // Load FilterMenu (Left Side) FXML
        URL filterMenuFxmlUrl = getClass().getResource("/filtermenu.fxml");
        if (filterMenuFxmlUrl == null) {
            System.out.println("filtermenu.fxml not found");
            return;
        }
        FXMLLoader filterMenuLoader = new FXMLLoader(filterMenuFxmlUrl, resources);
        FilterMenuController filterMenuController = new FilterMenuController(controller, eventDispatcher, resources);
        filterMenuLoader.setControllerFactory(c -> filterMenuController);
        Parent leftSection = filterMenuLoader.load();

        // Load Pipeline (Right Side) FXML
        URL pipelineFxmlUrl = getClass().getResource("/pipeline.fxml");
        if (pipelineFxmlUrl == null) {
            System.out.println("pipeline.fxml not found");
            return;
        }
        FXMLLoader pipelineLoader = new FXMLLoader(pipelineFxmlUrl);

        // Ottieni il singleton di PipelineController
        PipelineController pipelineController = PipelineController.getInstance();
        pipelineController.initializeDependencies(controller, eventDispatcher, resources);
        pipelineLoader.setControllerFactory(c -> pipelineController);  // Set the singleton as the controller
        Parent rightBox = pipelineLoader.load();
        pipelineController.setImageController(controller);  // Set the image controller in PipelineController

        // Link PipelineController with FilterMenuController
        filterMenuController.setPipelineController(pipelineController);

        // Load Center Canvas Section (Image View)
        Canvas canvas = controller.getCanvas();
        canvas.setManaged(false);
        canvas.setWidth(500);
        canvas.setHeight(400);

        StackPane canvasPane = new StackPane(canvas);
        canvasPane.setAlignment(Pos.CENTER);

        // Create Separators for left and right of the canvas
        Separator leftSeparator = new Separator(Orientation.VERTICAL);
        Separator rightSeparator = new Separator(Orientation.VERTICAL);

        HBox centerBox = new HBox(leftSeparator, canvasPane, rightSeparator);
        centerBox.setSpacing(10);
        HBox.setHgrow(canvasPane, Priority.ALWAYS);

        // Construct the BorderPane layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setLeft(leftSection);
        borderPane.setRight(rightBox);
        borderPane.setCenter(centerBox);
        borderPane.setBottom(infoBar);

        // Load window size from preferences
        double windowWidth = preferencesModel.getWindowWidth();
        double windowHeight = preferencesModel.getWindowHeight();

        // Create Scene with BorderPane
        Scene scene = new Scene(borderPane, windowWidth, windowHeight);

        // Set up Stage
        stage.setTitle("2D-Image-Editor");
        stage.setResizable(true);
        stage.setScene(scene);

        // Handle window close request to save preferences
        stage.setOnCloseRequest((WindowEvent we) -> savePreferences(stage));
        stage.show();
        stage.toFront();
    }

    // Method to save preferences (window size and locale)
    public static void savePreferences(Stage stage) {
        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();
        preferencesModel.setWindowSize(sceneWidth, sceneHeight);
        preferencesModel.setLocale(LocalizationModel.getInstance().getLocale());
        preferencesModel.save();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

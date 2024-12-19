package ch.supsi.os.frontend.model;

import ch.supsi.os.backend.model.LocalizationModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

public class PreferencesModel implements PreferencesHandler {
    private static PreferencesModel instance;
    private final static String preferencesFileName = "user.prefs";
    private final static Path preferencesPath = Paths.get(System.getProperty("user.home"), ".imageditor", preferencesFileName);
    private final Properties properties;

    // Keys for window size
    private final static String WINDOW_WIDTH_KEY = "windowWidth";
    private final static String WINDOW_HEIGHT_KEY = "windowHeight";

    // Key for last opened folder
    private final static String LAST_OPENED_FOLDER_KEY = "lastOpenedFolder";

    public static PreferencesModel getInstance() {
        if (instance == null) {
            instance = new PreferencesModel();
        }
        return instance;
    }

    // Load the last opened folder from preferences
    public Path getLastOpenedFolder() {
        String folderPath = properties.getProperty(LAST_OPENED_FOLDER_KEY);
        if (folderPath != null && !folderPath.isEmpty()) {
            return Paths.get(folderPath);
        }
        return Paths.get(System.getProperty("user.home")); // Default to the user's home folder
    }

    // Save the last opened folder to preferences
    public void setLastOpenedFolder(Path folderPath) {
        properties.setProperty(LAST_OPENED_FOLDER_KEY, folderPath.toString());
    }

    private void load() {
        createPreferencesFolder();
        try {
            properties.load(Files.newInputStream(preferencesPath));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public Locale getLocale() {
        String language = properties.getProperty("language");
        Locale fallback = Locale.US; // Default locale
        Locale locale = language == null ? fallback : Locale.forLanguageTag(language);

        // Validate the locale to ensure it corresponds to a valid ResourceBundle
        return LocalizationModel.getInstance().isInitialized()
                ? LocalizationModel.getInstance().getLocales().contains(locale) ? locale : fallback
                : fallback;
    }

    public void setLocale(Locale locale) {
        properties.setProperty("language", locale.toLanguageTag());
    }

    // Window size methods
    public double getWindowWidth() {
        String width = properties.getProperty(WINDOW_WIDTH_KEY);
        return width == null ? 900 : Double.parseDouble(width); // Default width is 900 if not found
    }

    public double getWindowHeight() {
        String height = properties.getProperty(WINDOW_HEIGHT_KEY);
        return height == null ? 700 : Double.parseDouble(height); // Default height is 700 if not found
    }

    public void setWindowSize(double width, double height) {
        properties.setProperty(WINDOW_WIDTH_KEY, String.valueOf(width));
        properties.setProperty(WINDOW_HEIGHT_KEY, String.valueOf(height));
    }

    @Override
    public void setDefaultDirectoryPath(Path path) {
        properties.setProperty("defaultSaveDirectory", path.toString());
    }

    @Override
    public Path getDefaultDirectoryPath(){
        String path = properties.getProperty("defaultSaveDirectory");
        if(path == null) {
            path = preferencesPath.getParent().toString();
        }
        File file = new File(path);
        // check if path does not exist
        if(!file.exists()){
            file.mkdirs();
        }
        return file.toPath();
    }

    private PreferencesModel() {
        properties = new Properties();
        load();
    }

    private void createPreferencesFolder() {
        try {
            // Create the directories if they don't exist
            if (!Files.exists(preferencesPath.getParent())) {
                Files.createDirectories(preferencesPath.getParent());
            }

            // Create the preferences file if it doesn't exist
            if (!Files.exists(preferencesPath)) {
                Files.createFile(preferencesPath);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void save() {
        createPreferencesFolder(); // Ensure folder and file exist before saving
        try {
            properties.store(Files.newOutputStream(preferencesPath), null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return properties != null;
    }
}

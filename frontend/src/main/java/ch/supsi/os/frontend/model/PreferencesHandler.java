package ch.supsi.os.frontend.model;

import java.nio.file.Path;
import java.util.Locale;

public interface PreferencesHandler extends Handler {
    void save();
    Locale getLocale();
    void setLocale(Locale locale);
    boolean isInitialized();
    void setDefaultDirectoryPath(Path path);
    Path getDefaultDirectoryPath();
}

package ch.supsi.os.backend.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalizationModel implements LocalizationHandler {

    private ObjectProperty<Locale> locale;

    private static LocalizationModel model;

    private boolean initialized;

    private String bundleName;

    protected LocalizationModel() {
        this.initialized = false;
    }

    public static LocalizationModel getInstance() {
        if (model == null) {
            model = new LocalizationModel();
        }
        return model;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void init(String bundleName, Locale locale) {
        if (initialized)
            throw new IllegalStateException("LocalizationModel already initialized");

        Locale validLocale = validateLocale(locale, bundleName);

        Locale.setDefault(validLocale);
        this.bundleName = bundleName;
        this.locale = new SimpleObjectProperty<>(validLocale);
        this.initialized = true;
    }

    private Locale validateLocale(Locale locale, String bundleName) {
        try {
            // Attempt to load the ResourceBundle for the given locale
            ResourceBundle.getBundle(bundleName, locale);
            return locale; // Return the locale if successful
        } catch (MissingResourceException e) {
            System.err.printf("Resource bundle not found for locale '%s'. Falling back to default locale.%n", locale);
            return Locale.US; // Fallback to a default locale
        }
    }

    @Override
    public List<Locale> getLocales() {
        return List.of(
                Locale.forLanguageTag("it-CH"),
                Locale.forLanguageTag("en-US"),
                Locale.forLanguageTag("de-DE"),
                Locale.forLanguageTag("fr-FR")
        );
    }

    public Locale getLocale() {
        return locale.get();
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    @Override
    public String localize(String key, Object... args) {
        String translation;
        try {
            translation = String.format(ResourceBundle.getBundle(bundleName, locale.get()).getString(key), args);
        } catch (MissingResourceException e) {
            translation = key;
        }
        return translation;
    }
}

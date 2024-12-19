package ch.supsi.os.backend.business;

import ch.supsi.os.backend.model.LocalizationHandler;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

public class LocalizationService {

    private static final String LOCALE_PREF_KEY = "preferred_locale";
    private static LocalizationHandler localizationHandler;

    public static void init(LocalizationHandler localizationHandler) {
        LocalizationService.localizationHandler = localizationHandler;

        // Load and set the saved locale on initialization
        Locale savedLocale = getSavedLocale();
        localizationHandler.setLocale(savedLocale);
    }

    public static List<Locale> getLocales() {
        if (localizationHandler != null && localizationHandler.isInitialized()) {
            return localizationHandler.getLocales();
        }
        return null;
    }

    public static void setLocale(Locale locale) {
        // Save the selected locale preference but do not update UI dynamically
        saveLocalePreference(locale);
    }

    private static void saveLocalePreference(Locale locale) {
        Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
        prefs.put(LOCALE_PREF_KEY, locale.toLanguageTag());
    }

    static Locale getSavedLocale() {
        Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
        String localeTag = prefs.get(LOCALE_PREF_KEY, Locale.getDefault().toLanguageTag());
        return Locale.forLanguageTag(localeTag);
    }

    public static String getLocalizedString(String key, Object... args) {
        if (localizationHandler != null && localizationHandler.isInitialized()) {
            return localizationHandler.localize(key, args);
        }
        return key; // Return the key itself if localization is missing
    }
}

package ch.supsi.os.backend.business;

import ch.supsi.os.backend.model.LocalizationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocalizationServiceTest {

    private LocalizationHandler mockLocalizationHandler;

    @BeforeEach
    public void setUp() {
        // Mock the LocalizationHandler
        mockLocalizationHandler = mock(LocalizationHandler.class);

        // Initialize the LocalizationService with the mocked handler
        LocalizationService.init(mockLocalizationHandler);
    }

    @Test
    public void testGetLocales_WhenHandlerIsInitialized() {
        // Prepare mock data
        List<Locale> mockLocales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        when(mockLocalizationHandler.isInitialized()).thenReturn(true);
        when(mockLocalizationHandler.getLocales()).thenReturn(mockLocales);

        // Call getLocales and verify result
        List<Locale> locales = LocalizationService.getLocales();
        assertNotNull(locales);
        assertEquals(2, locales.size());
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));

        // Verify interaction with the handler
        verify(mockLocalizationHandler).getLocales();
    }

    @Test
    public void testGetLocales_WhenHandlerIsNotInitialized() {
        // When handler is not initialized
        when(mockLocalizationHandler.isInitialized()).thenReturn(false);

        // Call getLocales and verify result is null
        assertNull(LocalizationService.getLocales());
    }

    @Test
    public void testSetAndSaveLocalePreference() {
        // Prepare mock locale
        Locale locale = Locale.ITALIAN;

        // Set locale preference and check that it is saved in Preferences
        LocalizationService.setLocale(locale);
        Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
        String savedLocaleTag = prefs.get("preferred_locale", null);

        assertEquals(locale.toLanguageTag(), savedLocaleTag);
    }

    @Test
    public void testGetSavedLocale_WhenSaved() {
        // Prepare mock locale to save in Preferences
        Locale locale = Locale.FRENCH;
        Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
        prefs.put("preferred_locale", locale.toLanguageTag());

        // Check that the saved locale is returned
        Locale savedLocale = LocalizationService.getSavedLocale();
        assertEquals(locale, savedLocale);
    }

    @Test
    public void testGetSavedLocale_WhenNoLocaleIsSaved() {
        // Clear any saved preferences
        Preferences prefs = Preferences.userNodeForPackage(LocalizationService.class);
        prefs.remove("preferred_locale");

        // Verify default locale is returned when none is saved
        Locale defaultLocale = Locale.getDefault();
        Locale savedLocale = LocalizationService.getSavedLocale();
        assertEquals(defaultLocale, savedLocale);
    }

    @Test
    public void testGetLocalizedString_WhenHandlerIsInitialized() {
        // Prepare mock localized string
        String key = "greeting";
        String localizedString = "Hello";
        when(mockLocalizationHandler.isInitialized()).thenReturn(true);
        when(mockLocalizationHandler.localize(key)).thenReturn(localizedString);

        // Call getLocalizedString and verify result
        String result = LocalizationService.getLocalizedString(key);
        assertEquals(localizedString, result);
        verify(mockLocalizationHandler).localize(key);
    }

    @Test
    public void testGetLocalizedString_WhenHandlerIsNotInitialized() {
        // When handler is not initialized
        String key = "greeting";
        when(mockLocalizationHandler.isInitialized()).thenReturn(false);

        // Call getLocalizedString and verify it returns the key itself
        String result = LocalizationService.getLocalizedString(key);
        assertEquals(key, result);
    }
}

package ch.supsi.os.backend.model;

import java.util.Locale;
import java.util.List;

public interface LocalizationHandler extends Handler {

    void init(String bundleName, Locale locale);

    List<Locale> getLocales();

    void setLocale(Locale locale);

    String localize(String key, Object... args);
}

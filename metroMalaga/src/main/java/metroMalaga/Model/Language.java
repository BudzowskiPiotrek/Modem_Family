package metroMalaga.Model;

import metroMalaga.Controller.TranslateService;

/**
 * Static wrapper for language translation services.
 */
public class Language {

    private static TranslateService translate;

    /**
     * Gets the singleton instance of TranslateService.
     * 
     * @return The TranslateService instance.
     */
    public static TranslateService getTranslate() {
        if (translate == null) {
            translate = new TranslateService();
        }
        return translate;
    }

    /**
     * Retrieves a translated string key by ID.
     * 
     * @param id The ID of the string resource.
     * @return The translated string.
     */
    public static String get(int id) {
        return getTranslate().get(id);
    }

    /**
     * Sets the current language to Spanish.
     */
    public static void setSpanish() {
        getTranslate().setSpanish();
    }

    /**
     * Sets the current language to English.
     */
    public static void setEnglish() {
        getTranslate().setEnglish();
    }

    /**
     * Gets the current language code.
     * 
     * @return The current language code (e.g., "Español", "English").
     */
    public static String getCurrentLanguage() {
        return getTranslate().getCurrentLanguage();
    }
}
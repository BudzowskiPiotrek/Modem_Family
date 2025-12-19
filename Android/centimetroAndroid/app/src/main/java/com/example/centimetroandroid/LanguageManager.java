package com.example.centimetroandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class LanguageManager {

    private static final String PREFS_NAME = "AppPreferences";
    private static final String KEY_LANGUAGE = "app_language";
    private static final String DEFAULT_LANGUAGE = "en";

    private Context context;
    private SharedPreferences prefs;

    public LanguageManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get current selected language
     * 
     * @return "en" or "es"
     */
    public String getCurrentLanguage() {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    /**
     * Set new language
     * 
     * @param languageCode "en" or "es"
     */
    public void setLanguage(String languageCode) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    /**
     * Get the manual collection name based on current language
     * 
     * @return "manual_en" or "manual_es"
     */
    public String getManualCollection() {
        return "manual_" + getCurrentLanguage();
    }

    /**
     * Get the UI strings collection name based on current language
     * 
     * @return "ui_strings_en" or "ui_strings_es"
     */
    public String getUIStringsCollection() {
        return "ui_strings_" + getCurrentLanguage();
    }

    /**
     * Toggle between English and Spanish
     */
    public void toggleLanguage() {
        String currentLang = getCurrentLanguage();
        String newLang = currentLang.equals("en") ? "es" : "en";
        setLanguage(newLang);
    }

    /**
     * Check if current language is English
     * 
     * @return true if English
     */
    public boolean isEnglish() {
        return getCurrentLanguage().equals("en");
    }

    /**
     * Check if current language is Spanish
     * 
     * @return true if Spanish
     */
    public boolean isSpanish() {
        return getCurrentLanguage().equals("es");
    }
}

package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for handling application localization and translations.
 */
public class TranslateService {

    private ConnecionSQL conSQL = new ConnecionSQL();
    private String currentLanguage = "espanol";
    private Map<Integer, String> translations = new HashMap<>();

    /**
     * Constructor for TranslateService. Initialize with Spanish.
     */
    public TranslateService() {
        loadTranslations("espanol");
    }

    /**
     * Loads translations from the database for the specified language.
     * 
     * @param languageColumn The database column name for the language (e.g.,
     *                       "espanol", "ingles").
     */
    public void loadTranslations(String languageColumn) {
        this.currentLanguage = languageColumn;
        translations.clear();

        String SQL = "SELECT id, " + languageColumn + " AS texto FROM traducciones";

        try (Connection con = conSQL.connect();
                PreparedStatement ps = con.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                translations.put(rs.getInt("id"), rs.getString("texto"));
            }

        } catch (SQLException e) {
            System.err.println("Error cargando traducciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a translated string by its ID.
     * 
     * @param id The ID of the translation string.
     * @return The translated string, or a placeholder if not found.
     */
    public String get(int id) {
        String translation = translations.get(id);

        if (translation == null) {
            System.err.println("⚠ Traducción no encontrada para ID: " + id);
            return "[ID:" + id + "]";
        }

        return translation;
    }

    /**
     * Sets the current language and reloads translations.
     * 
     * @param languageColumn The language column name.
     */
    public void setLanguage(String languageColumn) {
        loadTranslations(languageColumn);
    }

    /**
     * Gets the currently active language.
     * 
     * @return The language code/column name.
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * switch language to Spanish.
     */
    public void setSpanish() {
        setLanguage("espanol");
    }

    /**
     * Switch language to English.
     */
    public void setEnglish() {
        setLanguage("ingles");
    }
}
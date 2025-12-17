package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TranslateService {
    
    private ConnecionSQL conSQL = new ConnecionSQL();
    private String currentLanguage = "espanol";
    private Map<Integer, String> translations = new HashMap<>();
    
    public TranslateService() {
        loadTranslations("espanol");
    }

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
            
            System.out.println("✓ Cargadas " + translations.size() + " traducciones en " + languageColumn);
            
        } catch (SQLException e) {
            System.err.println("Error cargando traducciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String get(int id) {
        String translation = translations.get(id);
        
        if (translation == null) {
            System.err.println("⚠ Traducción no encontrada para ID: " + id);
            return "[ID:" + id + "]";
        }
        
        return translation;
    }
    

    public void setLanguage(String languageColumn) {
        loadTranslations(languageColumn);
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public void setSpanish() {
        setLanguage("espanol");
    }
    
    public void setEnglish() {
        setLanguage("ingles");
    }
}

package metroMalaga.backend.smtp;

import java.util.Properties;

public class EmailConfig {
    // Configuración para ENVIAR (SMTP)
    public static Properties getSmtpProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); // Cambiar si es otro proveedor
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // Necesario para Gmail/Outlook moderno
        // prop.put("mail.debug", "true"); // Descomenta para ver logs en consola si algo falla
        return prop;
    }

    // Configuración para RECIBIR (IMAP)
    public static Properties getImapProperties() {
        Properties prop = new Properties();
        prop.put("mail.store.protocol", "imaps");
        prop.put("mail.imaps.host", "imap.gmail.com"); // Cambiar si es otro proveedor
        prop.put("mail.imaps.port", "993");
        // prop.put("mail.debug", "true");
        return prop;
    }
}
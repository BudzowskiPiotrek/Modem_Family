package metroMalaga.backend.smtp;

import java.util.Properties;

public class EmailConfig {
    // Configuración para ENVIAR (SMTP)
    public static Properties getSmtpProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); 
        return prop;
    }

    // Configuración para RECIBIR (IMAP)
    public static Properties getImapProperties() {
        Properties prop = new Properties();
        prop.put("mail.store.protocol", "imaps");
        prop.put("mail.imaps.host", "imap.gmail.com"); 
        prop.put("mail.imaps.port", "993");

        return prop;
    }
}

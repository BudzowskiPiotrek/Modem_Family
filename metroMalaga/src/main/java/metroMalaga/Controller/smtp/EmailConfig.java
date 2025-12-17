package metroMalaga.Controller.smtp;

import java.util.Properties;

public class EmailConfig {

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String POP3_HOST = "pop.gmail.com";
    public static final String IMAP_HOST = "imap.gmail.com";

    public static Properties getSmtpProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return props;
    }

    public static Properties getImapProperties() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.protocols", "TLSv1.2");
        return props;
    }

    public static Properties getPop3Properties() {
        Properties props = new Properties();
        props.put("mail.pop3.host", POP3_HOST);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        
        props.put("mail.pop3.ssl.enable", "true");
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.socketFactory.fallback", "false");
        
        return props;
    }
}
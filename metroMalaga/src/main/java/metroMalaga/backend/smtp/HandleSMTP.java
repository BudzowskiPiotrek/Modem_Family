package metroMalaga.backend.smtp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import metroMalaga.Clases.EmailModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HandleSMTP {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String POP3_HOST = "pop.gmail.com";
    
    private String userEmail;
    private String appPassword;

    // Login method
    public boolean login(String email, String password) {
        this.userEmail = email;
        this.appPassword = password;
        return true; 
    }

    /**
     * SEND EMAIL (SMTP)
     */
    public void sendEmail(String recipient, String subject, String body) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, appPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    public List<EmailModel> fetchEmails() throws Exception {
        List<EmailModel> emailList = new ArrayList<>();

        Properties props = new Properties();
        props.put("mail.pop3.host", POP3_HOST);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("pop3");
        
        // Recuerda mantener el "recent:" si usas Gmail
        store.connect(POP3_HOST, "recent:" + userEmail, appPassword);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        int totalMessages = emailFolder.getMessageCount();
        
        // Si quieres ver MÁS correos, aumenta este rango (ej. los últimos 30)
        int start = Math.max(1, totalMessages - 30); 
        
        if (totalMessages > 0) {
            Message[] messages = emailFolder.getMessages(start, totalMessages);
            
            // Recorremos hacia atrás (del más nuevo al más viejo)
            for (int i = messages.length - 1; i >= 0; i--) {
                Message msg = messages[i];
                
                Address[] fromAddresses = msg.getFrom();
                String sender = (fromAddresses != null && fromAddresses.length > 0) 
                                ? fromAddresses[0].toString() : "Unknown";

                // --- NUEVO FILTRO: NO MOSTRAR LOS ENVIADOS POR MÍ ---
                // Si el remitente contiene mi propio correo, saltamos al siguiente ciclo
                if (sender.contains(userEmail)) {
                    continue; 
                }
                // ----------------------------------------------------

                String contentText = "No Content";
                try {
                    Object content = msg.getContent();
                    if (content instanceof String) {
                        contentText = (String) content;
                    } else if (content instanceof Multipart) {
                        Multipart multipart = (Multipart) content;
                        if(multipart.getCount() > 0) {
                            // A veces el cuerpo puede ser complejo, cogemos la primera parte
                            BodyPart bodyPart = multipart.getBodyPart(0);
                            contentText = bodyPart.getContent().toString();
                        }
                    }
                } catch (Exception e) { contentText = "[Complex Content]"; }

                emailList.add(new EmailModel(msg.getMessageNumber(), sender, msg.getSubject(), contentText));
            }
        }

        emailFolder.close(false);
        store.close();
        return emailList;
    }

    /**
     * DELETE EMAIL
     */
    /**
     * DELETE EMAIL (SAFE VERSION)
     * Busca el correo por Asunto y Remitente para evitar errores de índice POP3
     */
    public void deleteEmail(String subjectToDelete, String senderToDelete) throws Exception {
        Properties props = new Properties();
        props.put("mail.pop3.host", POP3_HOST);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("pop3");
     // Añadimos "recent:" antes del email
        store.connect(POP3_HOST, "recent:" + userEmail, appPassword);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_WRITE); // Necesario para borrar

        Message[] messages = emailFolder.getMessages();

        // OPTIMIZACIÓN: Cargar solo encabezados (rápido) para no bajar el cuerpo
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fp);

        boolean found = false;

        // Recorremos buscando el correo correcto
        for (Message msg : messages) {
            try {
                // Obtenemos el asunto (manejando nulos)
                String currentSubject = (msg.getSubject() == null) ? "" : msg.getSubject();
                
                // Obtenemos el remitente tal cual lo guardaste en fetchEmails
                Address[] fromAddresses = msg.getFrom();
                String currentSender = (fromAddresses != null && fromAddresses.length > 0) 
                                       ? fromAddresses[0].toString() : "Unknown";

                // COMPROBACIÓN: ¿Coinciden Asunto y Remitente?
                if (currentSubject.equals(subjectToDelete) && currentSender.equals(senderToDelete)) {
                    msg.setFlag(Flags.Flag.DELETED, true); // Marcar para borrar
                    found = true;
                    break; // Ya lo encontramos, dejamos de buscar
                }
            } catch (Exception e) {
                // Si un mensaje da error al leer, lo saltamos y seguimos con el siguiente
                continue;
            }
        }

        if (found) {
            emailFolder.close(true); // 'true' confirma el borrado físico (Expunge)
        } else {
            emailFolder.close(false);
            throw new Exception("El correo no se encontró en el servidor (quizás ya fue borrado).");
        }
        
        store.close();
    }
 // --- NUEVO: MÉTODO HÍBRIDO (IMAP SOLO PARA ESTADO DE LECTURA) ---
    public void updateReadStatusIMAP(String subject, String sender, boolean markAsRead) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com"); // Hardcoded para no ensuciar tus constantes
        props.put("mail.imaps.port", "993");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", userEmail, appPassword);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_WRITE); // Escritura para poder cambiar flags

        // 1. Buscamos el correo igual que hicimos en el borrado
        Message[] messages = emailFolder.getMessages();
        
        // Optimización: Solo bajamos encabezados
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fp);

        // 2. Recorremos para encontrar el match
        for (Message msg : messages) {
            try {
                String currentSubject = (msg.getSubject() == null) ? "" : msg.getSubject();
                Address[] fromAddresses = msg.getFrom();
                String currentSender = (fromAddresses != null && fromAddresses.length > 0) 
                                       ? fromAddresses[0].toString() : "";

                // Usamos trim() e ignoreCase para asegurar que lo encontramos
                if (currentSubject.trim().equalsIgnoreCase(subject.trim()) && 
                    currentSender.contains(sender)) {
                    
                    // ¡AQUÍ ESTÁ EL CAMBIO!
                    // Si markAsRead es true -> Pone Flag.SEEN a true (Leído)
                    // Si markAsRead es false -> Pone Flag.SEEN a false (No leído)
                    msg.setFlag(Flags.Flag.SEEN, markAsRead);
                    
                    System.out.println("IMAP: Estado actualizado para: " + currentSubject);
                    break; // Trabajo hecho, salimos
                }
            } catch (Exception e) { continue; }
        }

        emailFolder.close(true); // Guardamos cambios
        store.close();
    }
}
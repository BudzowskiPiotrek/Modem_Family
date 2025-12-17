package metroMalaga.Controller.smtp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import metroMalaga.Model.EmailModel;

import java.io.*;
import java.util.*;

public class HandleSMTP {

    private String userEmail;
    private String appPassword;

    public boolean login(String email, String password) {
        this.userEmail = email;
        this.appPassword = password;
        return true;
    }

    public List<EmailModel> fetchEmails() {
        List<EmailModel> emailList = new ArrayList<>();
        try {
            Session session = Session.getDefaultInstance(EmailConfig.getImapProperties());
            Store store = session.getStore("imaps");
            store.connect(EmailConfig.IMAP_HOST, userEmail, appPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            int totalMessages = emailFolder.getMessageCount();
            int start = Math.max(1, totalMessages - 50);

            if (totalMessages > 0) {
                Message[] messages = emailFolder.getMessages(start, totalMessages);
                
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                fp.add(FetchProfile.Item.FLAGS); 
                emailFolder.fetch(messages, fp);

                for (int i = messages.length - 1; i >= 0; i--) {
                    Message msg = messages[i];
                    Address[] fromAddresses = msg.getFrom();
                    String sender = (fromAddresses != null && fromAddresses.length > 0) ? fromAddresses[0].toString() : "Unknown";
                    
                    if (sender.contains(userEmail)) continue;

                    String uid = "";
                    if (msg instanceof MimeMessage) uid = ((MimeMessage) msg).getMessageID();

                    String bodyText = "[Click to load content...]";
                    EmailModel email = new EmailModel(msg.getMessageNumber(), sender, msg.getSubject(), bodyText);
                    email.setUniqueId(uid);
                    email.setRead(msg.isSet(Flags.Flag.SEEN));

                    emailList.add(email);
                }
            }
            emailFolder.close(false);
            store.close();
        } catch (Exception e) { e.printStackTrace(); }
        return emailList;
    }

    public void loadFullContent(EmailModel emailModel) {
        try {
            Session session = Session.getDefaultInstance(EmailConfig.getImapProperties());
            Store store = session.getStore("imaps");
            store.connect(EmailConfig.IMAP_HOST, userEmail, appPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            emailFolder.fetch(messages, fp);

            for (int i = messages.length - 1; i >= 0; i--) {
                if (messages[i] instanceof MimeMessage) {
                    String serverUid = ((MimeMessage) messages[i]).getMessageID();
                    if (serverUid.equals(emailModel.getUniqueId())) {
                        Message msg = messages[i];
                        
                        String bodyText = "";
                        List<String> attachments = new ArrayList<>();
                        try {
                            Object content = msg.getContent();
                            if (content instanceof Multipart) {
                                parseMultipart((Multipart) content, attachments);
                                bodyText = getTextFromMultipart((Multipart) content);
                            } else if (content instanceof String) {
                                bodyText = (String) content;
                            }
                        } catch (Exception e) { bodyText = "[Error loading content]"; }

                        emailModel.setContent(bodyText);
                        emailModel.setAttachmentNames(attachments);
                        break;
                    }
                }
            }
            emailFolder.close(false);
            store.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateReadStatusIMAP(String uid, boolean markAsRead) {
        try {
            Session session = Session.getDefaultInstance(EmailConfig.getImapProperties());
            Store store = session.getStore("imaps");
            store.connect(EmailConfig.IMAP_HOST, userEmail, appPassword);
            
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.getMessages();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            emailFolder.fetch(messages, fp);

            for (int i = messages.length - 1; i >= 0; i--) {
                if (((MimeMessage)messages[i]).getMessageID().equals(uid)) {
                    messages[i].setFlag(Flags.Flag.SEEN, markAsRead);
                    break;
                }
            }
            emailFolder.close(true);
            store.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteEmail(String uid) {
        try {
            Session session = Session.getDefaultInstance(EmailConfig.getImapProperties());
            Store store = session.getStore("imaps");
            store.connect(EmailConfig.IMAP_HOST, userEmail, appPassword);
            
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.getMessages();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            emailFolder.fetch(messages, fp);

            for (int i = messages.length - 1; i >= 0; i--) {
                if (((MimeMessage)messages[i]).getMessageID().equals(uid)) {
                    messages[i].setFlag(Flags.Flag.DELETED, true);
                    break;
                }
            }
            emailFolder.close(true);
            store.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void downloadEmailComplete(String uid, File destinationFile) throws Exception {
        Session session = Session.getInstance(EmailConfig.getPop3Properties());
        Store store = session.getStore("pop3");
        store.connect(EmailConfig.POP3_HOST, "recent:" + userEmail, appPassword);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(UIDFolder.FetchProfileItem.UID);
        emailFolder.fetch(messages, fp);

        Message targetMsg = null;
        for (int i = messages.length - 1; i >= 0; i--) {
            if (messages[i] instanceof MimeMessage) {
                String currentId = ((MimeMessage) messages[i]).getMessageID();
                if (currentId != null && currentId.equals(uid)) {
                    targetMsg = messages[i];
                    break;
                }
            }
        }
        
        if (targetMsg == null) {
            emailFolder.close(false);
            store.close();
            throw new Exception("Mensaje no encontrado en POP3.");
        }

        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            targetMsg.writeTo(fos);
        }

        emailFolder.close(false);
        store.close();
    }

    public void sendEmail(String recipient, String subject, String body, List<File> attachments) throws Exception {
        Session session = Session.getInstance(EmailConfig.getSmtpProperties(), new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(userEmail, appPassword); }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        multipart.addBodyPart(messageBodyPart);

        if (attachments != null && !attachments.isEmpty()) {
            for (File file : attachments) {
                if (file.exists()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(file);
                    multipart.addBodyPart(attachPart);
                }
            }
        }
        message.setContent(multipart);
        Transport.send(message);
    }

    private void parseMultipart(Multipart multipart, List<String> attachments) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) || (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
                    attachments.add(bodyPart.getFileName());
                } else if (bodyPart.getContent() instanceof Multipart) {
                    parseMultipart((Multipart) bodyPart.getContent(), attachments);
                }
            }
        } catch (Exception e) {}
    }

    private String getTextFromMultipart(Multipart multipart) {
        StringBuilder result = new StringBuilder();
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) || (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
                    continue; 
                }
                if (bodyPart.isMimeType("text/plain")) {
                    result.append(bodyPart.getContent());
                } else if (bodyPart.getContent() instanceof Multipart) {
                    result.append(getTextFromMultipart((Multipart) bodyPart.getContent()));
                }
            }
        } catch (Exception e) {}
        return result.toString();
    }
}
package metroMalaga.Clases;

public class EmailModel {
    private int messageNumber; // ID for POP3 deletion
    private String sender;
    private String subject;
    private String content;
    private boolean isRead;

    public EmailModel(int messageNumber, String sender, String subject, String content) {
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.isRead = false; // Default: Unread
    }

    // Getters and Setters
    public int getMessageNumber() { return messageNumber; }
    
    public String getSender() { return sender; }
    
    public String getSubject() { return subject; }
    
    public String getContent() { return content; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    @Override
    public String toString() {
        return subject;
    }
}
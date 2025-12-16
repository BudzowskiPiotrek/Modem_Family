package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;

public class ToggleReadStatusTask implements Runnable {
    
    private final HandleSMTP backend;
    private final String subject;
    private final String sender;
    private final String uniqueId;
    private final boolean newStatus;
    
    public ToggleReadStatusTask(HandleSMTP backend, String subject, String sender, 
                               String uniqueId, boolean newStatus) {
        this.backend = backend;
        this.subject = subject;
        this.sender = sender;
        this.uniqueId = uniqueId;
        this.newStatus = newStatus;
    }
    
    @Override
    public void run() {
        try {
            backend.updateReadStatusIMAP(subject, sender, uniqueId, newStatus);
        } catch (Exception ex) {
        }
    }
}

package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DownloadEmailTask implements Runnable {
    
    private final HandleSMTP backend;
    private final String uniqueId;
    private final File destination;
    private final Component parentView;
    
    public DownloadEmailTask(HandleSMTP backend, String uniqueId, 
                            File destination, Component parentView) {
        this.backend = backend;
        this.uniqueId = uniqueId;
        this.destination = destination;
        this.parentView = parentView;
    }
    
    @Override
    public void run() {
        try {
            backend.downloadEmailComplete(uniqueId, destination);
            JOptionPane.showMessageDialog(parentView, 
                "Email Downloaded: " + destination.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentView, 
                "Error: " + ex.getMessage());
        }
    }
}

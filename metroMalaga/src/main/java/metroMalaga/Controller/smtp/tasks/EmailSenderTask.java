package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class EmailSenderTask implements Runnable {
    
    private final HandleSMTP backend;
    private final String recipient;
    private final String subject;
    private final String body;
    private final List<File> attachments;
    private final Component parentView;
    private final JButton btnSend;
    private final JTextField txtTo;
    private final JTextField txtSubject;
    private final JTextArea txtBody;
    private final JLabel lblAttachedFile;
    private final List<File> attachmentsList;
    
    public EmailSenderTask(HandleSMTP backend, String recipient, String subject, 
                          String body, List<File> attachments, Component parentView, 
                          JButton btnSend, JTextField txtTo, JTextField txtSubject, 
                          JTextArea txtBody, JLabel lblAttachedFile, List<File> attachmentsList) {
        this.backend = backend;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
        this.parentView = parentView;
        this.btnSend = btnSend;
        this.txtTo = txtTo;
        this.txtSubject = txtSubject;
        this.txtBody = txtBody;
        this.lblAttachedFile = lblAttachedFile;
        this.attachmentsList = attachmentsList;
    }
    
    @Override
    public void run() {
        try {
            btnSend.setEnabled(false);
            btnSend.setText("SENDING...");
            
            backend.sendEmail(recipient, subject, body, attachments);
            
            JOptionPane.showMessageDialog(parentView, "Email Sent.");
            txtTo.setText("");
            txtSubject.setText("");
            txtBody.setText("");
            
            attachmentsList.clear();
            lblAttachedFile.setText("NO FILES");
            lblAttachedFile.setForeground(new Color(150, 150, 150));
            lblAttachedFile.setToolTipText(null);
            
            btnSend.setText("SEND EMAIL");
            btnSend.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentView, 
                "Error: " + ex.getMessage(), 
                "CRITICAL ERROR",
                JOptionPane.ERROR_MESSAGE);
            btnSend.setText("SEND EMAIL");
            btnSend.setEnabled(true);
        }
    }
}

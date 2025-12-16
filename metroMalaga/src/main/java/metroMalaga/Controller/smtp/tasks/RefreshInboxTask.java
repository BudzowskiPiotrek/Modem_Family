package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RefreshInboxTask implements Runnable {
    
    private final HandleSMTP backend;
    private final JButton btnRefresh;
    private final DefaultTableModel tableModel;
    private final JTextArea txtViewer;
    private final List<EmailModel> currentEmailList;
    
    public RefreshInboxTask(HandleSMTP backend, JButton btnRefresh, 
                           DefaultTableModel tableModel, JTextArea txtViewer,
                           List<EmailModel> currentEmailList) {
        this.backend = backend;
        this.btnRefresh = btnRefresh;
        this.tableModel = tableModel;
        this.txtViewer = txtViewer;
        this.currentEmailList = currentEmailList;
    }
    
    @Override
    public void run() {
        btnRefresh.setEnabled(false);
        tableModel.setRowCount(0);
        txtViewer.setText("Syncing emails...");
        
        try {
            List<EmailModel> mails = backend.fetchEmails();
            currentEmailList.clear();
            currentEmailList.addAll(mails);
            
            for (EmailModel mail : mails) {
                String status = mail.isRead() ? "SEEN" : "NEW";
                String subjectDisplay = mail.getSubject() + (mail.hasAttachments() ? " [FILE]" : "");
                tableModel.addRow(new Object[] { status, mail.getSender(), subjectDisplay });
            }
            txtViewer.setText("Cognition Updated.\n" + mails.size() + " messages.");
            btnRefresh.setEnabled(true);
        } catch (Exception ex) {
            txtViewer.setText("Connection Broken.");
            btnRefresh.setEnabled(true);
        }
    }
}

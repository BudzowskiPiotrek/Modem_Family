package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DeleteEmailTask implements Runnable {
    
    private final HandleSMTP backend;
    private final String subject;
    private final String sender;
    private final int row;
    private final List<EmailModel> currentEmailList;
    private final DefaultTableModel tableModel;
    private final JTextArea txtViewer;
    
    public DeleteEmailTask(HandleSMTP backend, String subject, String sender, int row,
                          List<EmailModel> currentEmailList, DefaultTableModel tableModel,
                          JTextArea txtViewer) {
        this.backend = backend;
        this.subject = subject;
        this.sender = sender;
        this.row = row;
        this.currentEmailList = currentEmailList;
        this.tableModel = tableModel;
        this.txtViewer = txtViewer;
    }
    
    @Override
    public void run() {
        try {
            backend.deleteEmail(subject, sender);
            currentEmailList.remove(row);
            tableModel.removeRow(row);
            txtViewer.setText("Email Eliminated.");
        } catch (Exception ex) {
        }
    }
}

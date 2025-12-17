package metroMalaga.Controller.smtp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;

import metroMalaga.Model.EmailModel;

public class MouseClickEmail extends MouseAdapter {

	private final JTable emailTable;
	private final JTextArea txtViewer;
	private final JButton btnDownloadEmail;
	private final List<EmailModel> currentEmailList;

	public MouseClickEmail(JTable emailTable, JTextArea txtViewer, JButton btnDownloadEmail,
			List<EmailModel> currentEmailList) {
		this.emailTable = emailTable;
		this.txtViewer = txtViewer;
		this.btnDownloadEmail = btnDownloadEmail;
		this.currentEmailList = currentEmailList;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == emailTable) {
			int row = emailTable.getSelectedRow();
			if (row >= 0 && row < currentEmailList.size()) {
				EmailModel mail = currentEmailList.get(row);

				String attachmentsInfo = "";
				if (mail.hasAttachments()) {
					attachmentsInfo = "\n\n=== ATTACHED OBJECTS ===\n";
					for (String n : mail.getAttachmentNames()) {
						attachmentsInfo += "> " + n + "\n";
					}
				}

				btnDownloadEmail.setEnabled(true);
				txtViewer.setText("SOURCE: " + mail.getSender() + "\nTOPIC: " + mail.getSubject()
						+ "\n--------------------------------\n" + mail.getContent() + attachmentsInfo);
			}
		}
	}
}

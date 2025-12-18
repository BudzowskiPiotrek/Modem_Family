package metroMalaga.Controller.smtp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import metroMalaga.Controller.smtp.tasks.LoadContentTask;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;

public class EmailTableMouseListener extends MouseAdapter {

	private final HandleSMTP backend;
	private final ButtonHandleSMTP controller;
	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;
	private final JButton btnDownloadEmail;

	public EmailTableMouseListener(HandleSMTP backend, ButtonHandleSMTP controller, JTable emailTable,
			DefaultTableModel tableModel, JTextArea txtViewer, JButton btnDownloadEmail) {
		this.backend = backend;
		this.controller = controller;
		this.emailTable = emailTable;
		this.tableModel = tableModel;
		this.txtViewer = txtViewer;
		this.btnDownloadEmail = btnDownloadEmail;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == emailTable) {
			int row = emailTable.getSelectedRow();

			List<EmailModel> currentList = controller.getCurrentEmailList();

			if (row >= 0 && row < currentList.size()) {
				EmailModel mail = currentList.get(row);

				if (Language.get(160).equals(mail.getContent())) {
					txtViewer.setText(Language.get(161));

					LoadContentTask task = new LoadContentTask(backend, mail, txtViewer, btnDownloadEmail, tableModel,
							row);
					new Thread(task).start();
				} else {
					displayContent(mail);
				}
			}
		}
	}

	private void displayContent(EmailModel mail) {
		String attachmentsInfo = "";
		if (mail.hasAttachments()) {
			attachmentsInfo = Language.get(159);
			for (String n : mail.getAttachmentNames())
				attachmentsInfo += "> " + n + "\n";
		}
		btnDownloadEmail.setEnabled(true);
		txtViewer.setText(Language.get(157) + mail.getSender() + "\n" + Language.get(158) + mail.getSubject()
				+ "\n--------------------------------\n" + mail.getContent() + attachmentsInfo);
	}
}

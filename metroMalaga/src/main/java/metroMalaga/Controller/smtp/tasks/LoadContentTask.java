package metroMalaga.Controller.smtp.tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;

public class LoadContentTask implements Runnable {

	private final HandleSMTP backend;
	private final EmailModel mail;
	private final JTextArea txtViewer;
	private final JButton btnDownloadEmail;
	private final DefaultTableModel tableModel;
	private final int row;

	public LoadContentTask(HandleSMTP backend, EmailModel mail, JTextArea txtViewer, JButton btnDownloadEmail,
			DefaultTableModel tableModel, int row) {
		this.backend = backend;
		this.mail = mail;
		this.txtViewer = txtViewer;
		this.btnDownloadEmail = btnDownloadEmail;
		this.tableModel = tableModel;
		this.row = row;
	}

	@Override
	public void run() {
		backend.loadFullContent(mail);
		String att = "";
		if (mail.hasAttachments()) {
			att = Language.get(159);
			for (String n : mail.getAttachmentNames())
				att += "> " + n + "\n";
		}
		btnDownloadEmail.setEnabled(true);
		txtViewer.setText(Language.get(157) + mail.getSender() + "\n" + Language.get(158) + mail.getSubject()
				+ "\n--------------------------------\n" + mail.getContent() + att);
		tableModel.fireTableRowsUpdated(row, row);
	}
}

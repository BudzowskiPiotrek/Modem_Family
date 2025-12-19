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

/**
 * Mouse listener for the emails table.
 * Handles selection events to display email content.
 * Note: Should consider consolidating with MouseClickListener if they serve
 * redundant purposes.
 */
public class EmailTableMouseListener extends MouseAdapter {

	private final HandleSMTP backend;
	private final ButtonHandleSMTP controller;
	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;
	private final JButton btnDownloadEmail;

	/**
	 * Constructor.
	 * 
	 * @param backend          The backend service.
	 * @param controller       The main controller.
	 * @param emailTable       The table displaying emails.
	 * @param tableModel       The table model.
	 * @param txtViewer        The text area for viewing email content.
	 * @param btnDownloadEmail The button for downloading email.
	 */
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

	/**
	 * Formats and displays the email content.
	 * 
	 * @param mail The email to display.
	 */
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

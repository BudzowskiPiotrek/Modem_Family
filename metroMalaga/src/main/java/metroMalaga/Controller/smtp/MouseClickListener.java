package metroMalaga.Controller.smtp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import metroMalaga.Controller.smtp.tasks.LoadContentTask;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;

/**
 * Extended mouse listener for the emails table.
 * Handles "mark as read" logic in addition to displaying content.
 */
public class MouseClickListener extends MouseAdapter {

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
	public MouseClickListener(HandleSMTP backend, ButtonHandleSMTP controller, JTable emailTable,
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

				if (!mail.isRead()) {
					controller.addPendingId(mail.getUniqueId());

					mail.setRead(true);
					SwingUtilities.invokeLater(() -> tableModel.setValueAt(Language.get(153), row, 0));

					new Thread(() -> {
						try {
							backend.updateReadStatusIMAP(mail.getUniqueId(), true);
							Thread.sleep(2500);
						} catch (InterruptedException ex) {
						} finally {
							controller.removePendingId(mail.getUniqueId());
						}
					}).start();
				}

				if (Language.get(160).equals(mail.getContent())) {
					txtViewer.setText(Language.get(161));

					LoadContentTask task = new LoadContentTask(backend, mail, txtViewer, btnDownloadEmail, tableModel,
							row);
					new Thread(task).start();
				} else {
					controller.displayContent(mail);
				}
			}
		}
	}
}

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

public class MouseClickListener extends MouseAdapter {

	private final HandleSMTP backend;
	private final ButtonHandleSMTP controller;
	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;
	private final JButton btnDownloadEmail;

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

				if ("[Click to load content...]".equals(mail.getContent())) {
					txtViewer.setText("Downloading message content...\nPlease wait.");
					
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
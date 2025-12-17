package metroMalaga.Controller.smtp.tasks;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;

public class RefreshInboxTask implements Runnable {

	private final HandleSMTP backend;
	private final boolean isAuto;
	private final JButton btnRefresh;
	private final JTextArea txtViewer;
	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final List<EmailModel> currentEmailList;

	private static volatile boolean isRefreshing = false;

	public RefreshInboxTask(HandleSMTP backend, boolean isAuto, JButton btnRefresh, JTextArea txtViewer,
			JTable emailTable, DefaultTableModel tableModel, List<EmailModel> currentEmailList) {
		this.backend = backend;
		this.isAuto = isAuto;
		this.btnRefresh = btnRefresh;
		this.txtViewer = txtViewer;
		this.emailTable = emailTable;
		this.tableModel = tableModel;
		this.currentEmailList = currentEmailList;
	}

	@Override
	public void run() {
		if (isRefreshing)
			return;
		isRefreshing = true;

		if (!isAuto) {
			btnRefresh.setEnabled(false);
			txtViewer.setText("Checking new emails...");
		}

		try {
			List<EmailModel> mails = backend.fetchEmails();
			currentEmailList.clear();
			currentEmailList.addAll(mails);

			int selectedRow = emailTable.getSelectedRow();
			String selectedUid = (selectedRow != -1 && selectedRow < currentEmailList.size())
					? currentEmailList.get(selectedRow).getUniqueId()
					: null;

			tableModel.setRowCount(0);
			int newSel = -1;
			for (int i = 0; i < mails.size(); i++) {
				EmailModel m = mails.get(i);
				String st = m.isRead() ? "READ" : "UNREAD";
				String sub = m.getSubject() + (m.hasAttachments() ? " 📎" : "");
				tableModel.addRow(new Object[] { st, m.getSender(), sub });
				if (selectedUid != null && selectedUid.equals(m.getUniqueId()))
					newSel = i;
			}

			if (newSel != -1)
				emailTable.setRowSelectionInterval(newSel, newSel);

			if (!isAuto) {
				txtViewer.setText("Inbox updated. " + mails.size() + " messages.");
				btnRefresh.setEnabled(true);
			}
		} catch (Exception e) {
			if (!isAuto)
				txtViewer.setText("Error.");
			btnRefresh.setEnabled(true);
		} finally {
			isRefreshing = false;
		}
	}
}
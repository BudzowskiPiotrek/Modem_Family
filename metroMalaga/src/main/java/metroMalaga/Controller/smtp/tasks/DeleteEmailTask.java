package metroMalaga.Controller.smtp.tasks;

import java.util.List;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;

public class DeleteEmailTask implements Runnable {

	private final HandleSMTP backend;
	private final JTextArea txtViewer;
	private final List<EmailModel> currentEmailList;
	private final DefaultTableModel tableModel;
	private final String uid;
	private final int row;

	public DeleteEmailTask(HandleSMTP backend, JTextArea txtViewer, List<EmailModel> currentEmailList,
			DefaultTableModel tableModel, String uid, int row) {
		this.backend = backend;
		this.txtViewer = txtViewer;
		this.currentEmailList = currentEmailList;
		this.tableModel = tableModel;
		this.uid = uid;
		this.row = row;
	}

	@Override
	public void run() {
		try {
			backend.deleteEmail(uid);
			currentEmailList.remove(row);
			tableModel.removeRow(row);
			txtViewer.setText(Language.get(162));
		} catch (Exception e) {
		}
	}
}

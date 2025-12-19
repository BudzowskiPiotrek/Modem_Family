package metroMalaga.Controller.smtp.tasks;

import java.util.List;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;

/**
 * Task for deleting an email from the server and updating the UI.
 */
public class DeleteEmailTask implements Runnable {

	private final HandleSMTP backend;
	private final JTextArea txtViewer;
	private final List<EmailModel> currentEmailList;
	private final DefaultTableModel tableModel;
	private final String uid;
	private final int row;

	/**
	 * Constructor.
	 * 
	 * @param backend          The backend service.
	 * @param txtViewer        The text area viewer.
	 * @param currentEmailList The current list of emails.
	 * @param tableModel       The table model.
	 * @param uid              The unique ID of the mail to delete.
	 * @param row              The row index in the table.
	 */
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

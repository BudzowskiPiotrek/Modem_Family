package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

public class HandleFTPbtnFile implements ActionListener {

	private final JTable ftpTable;

	public HandleFTPbtnFile(JTable table) {
		this.ftpTable = table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow = ftpTable.getSelectedRow();
		String command = e.getActionCommand();
		if (selectedRow != -1) {
			if (command.equals("DELETE")) {

				
				
			} else if (command.equals("MODIFY")) {

				
				
				
			}
		}
	}
}
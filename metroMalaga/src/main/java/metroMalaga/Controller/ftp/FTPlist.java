package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Model.FTPTableModel;

public class FTPlist implements DocumentListener {

	private JTextField text;
	private FTPTableModel ftpTable;

	/**
	 * Constructor for FTPlist class.
	 * 
	 * @param text     The search text field.
	 * @param ftpTable The FTP table model.
	 */
	public FTPlist(JTextField text, FTPTableModel ftpTable) {
		this.ftpTable = ftpTable;
		this.text = text;
		this.text.getDocument().addDocumentListener(this);
	}

	/**
	 * Filters the table based on the search text.
	 */
	private void filterTable() {
		String searchText = text.getText().trim().toLowerCase();

		List<FTPFile> filteredList = new ArrayList<>();
		if (searchText.isEmpty()) {
			ftpTable.setData(ftpTable.getMasterList());
			return;
		}
		for (FTPFile file : ftpTable.getMasterList()) {
			if (file.isValid()) {

				String fileName = file.getName().toLowerCase();

				if (fileName.contains(searchText)) {
					filteredList.add(file);
				}
			}
		}

		ftpTable.setData(filteredList);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		filterTable();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		filterTable();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
}

package metroMalaga.Model;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;

/**
 * Table model for displaying FTP files in a JTable.
 */
public class FTPTableModel extends AbstractTableModel {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final ServiceFTP service;
	private List<FTPFile> masterList;
	private List<FTPFile> currentFileList;
	private String[] columnNames;

	/**
	 * Constructor for FTPTableModel.
	 * 
	 * @param initialList The initial list of FTP files.
	 * @param service     The FTP service instance.
	 */
	public FTPTableModel(List<FTPFile> initialList, ServiceFTP service) {
		this.masterList = initialList;
		this.currentFileList = initialList;
		this.service = service;
		updateColumnNames();
	}

	/**
	 * Updates the column names based on the current language.
	 */
	public void updateColumnNames() {
		this.columnNames = new String[] {
				Language.get(87),
				Language.get(88),
				Language.get(89),
				Language.get(90)
		};
		fireTableStructureChanged();
	}

	/**
	 * Sets custom column identifiers.
	 * 
	 * @param newIdentifiers Array of new column identifiers.
	 */
	public void setColumnIdentifiers(Object[] newIdentifiers) {
		columnNames = new String[newIdentifiers.length];
		for (int i = 0; i < newIdentifiers.length; i++) {
			columnNames[i] = newIdentifiers[i].toString();
		}
		fireTableStructureChanged();
	}

	/**
	 * Gets the master list of files.
	 * 
	 * @return The master list of FTP files.
	 */
	public List<FTPFile> getMasterList() {
		return masterList;
	}

	/**
	 * Sets the data for the table.
	 * 
	 * @param newData The new list of FTP files.
	 */
	public void setData(List<FTPFile> newData) {
		this.currentFileList = newData;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return currentFileList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		FTPFile file = currentFileList.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return file.getName();
			case 1:
				long size;
				if (file.isDirectory()) {
					size = service.calculateDirectorySize(file.getName());
				} else {
					size = file.getSize();
				}
				return Common.formatSize(size);
			case 2:
				Date date = file.getTimestamp().getTime();
				return DATE_FORMAT.format(date);
			case 3:
				return file;
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 3) {
			return FTPFile.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 3;
	}
}

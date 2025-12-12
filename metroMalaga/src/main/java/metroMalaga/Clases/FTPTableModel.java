package metroMalaga.Clases;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.net.ftp.FTPFile;

public class FTPTableModel extends AbstractTableModel {

	private List<FTPFile> masterList;
	private List<FTPFile> currentFileList;
	private final String[] columnNames = { "DATE", "NAME", "BUTTONS" };

	public FTPTableModel(List<FTPFile> initialList) {
		this.masterList = initialList;
		this.currentFileList = initialList;
	}

	public List<FTPFile> getMasterList() {
		return masterList;
	}

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
			return file.getTimestamp().getTime();
		case 1:
			return file.getName();
		case 2:
			return "";
		default:
			return null;
		}
	}
}
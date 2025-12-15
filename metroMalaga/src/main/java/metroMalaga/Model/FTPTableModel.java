package metroMalaga.Model;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;

public class FTPTableModel extends AbstractTableModel {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final ServiceFTP service;
	private List<FTPFile> masterList;
	private List<FTPFile> currentFileList;
	private final String[] columnNames = { "NAME", "SIZE", "DATE", "BUTTONS" };

	public FTPTableModel(List<FTPFile> initialList, ServiceFTP service) {
		this.masterList = initialList;
		this.currentFileList = initialList;
		this.service = service;
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
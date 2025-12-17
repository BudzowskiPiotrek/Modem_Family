package metroMalaga.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.net.ftp.FTPFile;

public class FTPTableModel extends AbstractTableModel {

    private List<FTPFile> files;
    private List<FTPFile> filteredFiles;

    public FTPTableModel(List<FTPFile> files) {
        this.files = new ArrayList<>(files);
        this.filteredFiles = new ArrayList<>(files);
    }

    @Override
    public int getRowCount() {
        return filteredFiles.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return Language.get(87 + column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FTPFile file = filteredFiles.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return file.getName();
            case 1:
                return file.isDirectory() ? "-" : formatSize(file.getSize());
            case 2:
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return sdf.format(file.getTimestamp().getTime());
            case 3:
                return "buttons";
            default:
                return null;
        }
    }

    private String formatSize(long size) {
        if (size < 1024)
            return size + " B";
        if (size < 1024 * 1024)
            return String.format("%.2f KB", size / 1024.0);
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }

    public void setData(List<FTPFile> newFiles) {
        this.files = new ArrayList<>(newFiles);
        this.filteredFiles = new ArrayList<>(newFiles);
        fireTableDataChanged();
    }

    public List<FTPFile> getMasterList() {
        return new ArrayList<>(files);
    }

    public void filterFiles(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredFiles = new ArrayList<>(files);
        } else {
            filteredFiles = new ArrayList<>();
            String lowerSearch = searchText.toLowerCase();
            for (FTPFile file : files) {
                if (file.getName().toLowerCase().contains(lowerSearch)) {
                    filteredFiles.add(file);
                }
            }
        }
        fireTableDataChanged();
    }

    public FTPFile getFileAt(int row) {
        if (row >= 0 && row < filteredFiles.size()) {
            return filteredFiles.get(row);
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }
}

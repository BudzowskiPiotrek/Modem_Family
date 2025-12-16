package metroMalaga.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Model.FTPTableModel;

public class FTPdoubleClick extends MouseAdapter {

    private final JTable ftpTable;
    private final ServiceFTP service;
    private final FTPTableModel model;

    public FTPdoubleClick(JTable table, ServiceFTP service, FTPTableModel model) {
        this.ftpTable = table;
        this.service = service;
        this.model = model;
        this.ftpTable.addMouseListener(this); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int row = ftpTable.getSelectedRow();
            if (row != -1) {
                int modelRow = ftpTable.convertRowIndexToModel(row);
                
                FTPFile selectedFile = (FTPFile) model.getValueAt(modelRow, 3); 
                
                handleNavigation(selectedFile);
            }
        }
    }

    private void handleNavigation(FTPFile file) {
        if (file.isDirectory()) {
            boolean success = service.changeDirectory(file.getName()); 
            
            if (success) {
                FTPFile[] updatedFilesArray = service.listAllFiles();
                List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));
                model.setData(updatedFilesList);
            } 
            
        } else {
            JOptionPane.showMessageDialog(null, "Esto es un archivo, no una carpeta. Usa 'Descargar' para obtenerlo.");
        }
    }
}

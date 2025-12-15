package metroMalaga.backend;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Clases.FTPTableModel;
import metroMalaga.frontend.ftp.ButtonsPanel;

public class ButtonsEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final ButtonsPanel panel; 
	private FTPFile currentFile;
	private final ServiceFTP service; 
	private final FTPTableModel model; 

	public ButtonsEditor(ServiceFTP service, FTPTableModel model) {
		this.service = service;
		this.model = model;
		this.panel = new ButtonsPanel(); 

		panel.downloadButton.addActionListener(this);
		panel.deleteButton.addActionListener(this);
		panel.renameButton.addActionListener(this);

		panel.downloadButton.setActionCommand("DOWNLOAD");
		panel.deleteButton.setActionCommand("DELETE");
		panel.renameButton.setActionCommand("RENAME");
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.currentFile = (FTPFile) value;
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return currentFile;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireEditingStopped();

		String command = e.getActionCommand();

		if (currentFile != null) {
			switch (command) {
			case "DOWNLOAD":
				handleDownload(currentFile);
				break;
			case "DELETE":
				handleDelete(currentFile);
				break;
			case "RENAME":
				handleRename(currentFile);
				break;
			}
		}
	}


	private void handleDownload(FTPFile file) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Guardar " + file.getName() + " como...");
		fileChooser.setSelectedFile(new java.io.File(file.getName()));

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			String localFilePath = fileChooser.getSelectedFile().getAbsolutePath();

			try {
				boolean success = service.downloadFile(file.getName(), localFilePath);

				if (success) {
					JOptionPane.showMessageDialog(null, "Archivo descargado con éxito a:\n" + localFilePath);
				} else {
					JOptionPane.showMessageDialog(null, "Fallo al descargar el archivo.", "Error de Descarga",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error de E/S durante la descarga: " + e.getMessage(),
						"Error de Descarga", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleDelete(FTPFile file) {
		int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres borrar " + file.getName() + "?",
				"Confirmar Borrado", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				boolean success = service.deleteFile(file.getName());

				if (success) {
					JOptionPane.showMessageDialog(null, "Archivo borrado con éxito.");
					updateTable(); 
				} else {
					JOptionPane.showMessageDialog(null, "Fallo al borrar el archivo.", "Error de Borrado",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error de E/S durante el borrado: " + e.getMessage(),
						"Error de Borrado", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleRename(FTPFile file) {
		String newName = JOptionPane.showInputDialog(null, "Nuevo nombre para " + file.getName() + ":");
		if (newName != null && !newName.trim().isEmpty()) {
			try {
				boolean success = service.renameFile(file.getName(), newName.trim());

				if (success) {
					JOptionPane.showMessageDialog(null, "Archivo renombrado con éxito.");
					updateTable();
				} else {
					JOptionPane.showMessageDialog(null, "Fallo al renombrar el archivo.", "Error de Renombrado",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error de E/S durante el renombrado: ",
						"Error de Renombrado", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void updateTable() {

		FTPFile[] updatedFilesArray = service.listAllFiles();
		List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));
		model.setData(updatedFilesList);
	}
}
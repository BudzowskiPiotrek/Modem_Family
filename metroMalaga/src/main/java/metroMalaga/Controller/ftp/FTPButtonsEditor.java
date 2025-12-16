package metroMalaga.Controller.ftp;

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

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;

public class FTPButtonsEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final FTPButtonsPanel panel;
	private FTPFile currentFile;
	private final ServiceFTP service;
	private final FTPTableModel model;

	public FTPButtonsEditor(ServiceFTP service, FTPTableModel model) {
		this.service = service;
		this.model = model;
		this.panel = new FTPButtonsPanel();

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
					JOptionPane.showMessageDialog(null, "File successfully downloaded to:\n" + localFilePath);
				} else {
					JOptionPane.showMessageDialog(null, "Error: The folder cannot be downloaded", "DownloadError",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "I/O error during download: " + e.getMessage(), "DownloadError",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleDelete(FTPFile file) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete it?" + file.getName() + "?",

				"Confirm Deletion", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				boolean success = service.deleteFile(file.getName());

				if (success) {
					JOptionPane.showMessageDialog(null, "File successfully deleted.");
					updateTable();
				} else {
					JOptionPane.showMessageDialog(null, "You can't delete the folder", "Error de Borrado",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "I/O error during erase: " + e.getMessage(), "Error de Borrado",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleRename(FTPFile file) {
		String newName = JOptionPane.showInputDialog(null, "New name for" + file.getName() + ":");
		if (newName != null && !newName.trim().isEmpty()) {
			try {
				boolean success = service.renameFile(file.getName(), newName.trim());

				if (success) {
					JOptionPane.showMessageDialog(null, "File renamed successfully.");
					updateTable();
				} else {
					JOptionPane.showMessageDialog(null, "Fallo al renombrar el archivo.", "Error de Renombrado",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error de E/S durante el renombrado: ", "Error de Renombrado",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void updateTable() {

		FTPFile[] updatedFilesArray = service.listAllFiles();
		List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));
		model.setData(updatedFilesList);
	}
}
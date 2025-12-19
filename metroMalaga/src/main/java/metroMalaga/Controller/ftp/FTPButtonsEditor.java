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

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

public class FTPButtonsEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final FTPButtonsPanel panel;
	private FTPFile currentFile;
	private final ServiceFTP service;
	private final FTPTableModel model;
	private Common cn;
	private Usuario user;

	/**
	 * Constructor for FTPButtonsEditor.
	 * 
	 * @param service The FTP service instance.
	 * @param model   The FTP table model.
	 * @param user    The current user.
	 */
	public FTPButtonsEditor(ServiceFTP service, FTPTableModel model, Usuario user) {
		this.cn = new Common();
		this.user = user;
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

		// Check if user can modify/delete this file
		String filename = currentFile.getName();
		boolean canDelete = metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(
				filename,
				user.getUsernameApp(),
				user.getRol(),
				true // checking delete permission
		);

		boolean canModify = metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(
				filename,
				user.getUsernameApp(),
				user.getRol(),
				false // checking modify/rename permission
		);

		boolean canDownload = metroMalaga.Model.FTPFileOwnershipDAO.canDownloadFile(user.getRol());

		// Show/hide buttons based on permissions
		panel.deleteButton.setVisible(canDelete);
		panel.renameButton.setVisible(canModify);
		panel.downloadButton.setVisible(canDownload);

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

	/**
	 * Handles button actions (Download, Delete, Rename).
	 * 
	 * @param e The ActionEvent.
	 */
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
		fileChooser.setDialogTitle(Language.get(105) + file.getName() + Language.get(106));
		fileChooser.setSelectedFile(new java.io.File(file.getName()));

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			String localFilePath = fileChooser.getSelectedFile().getAbsolutePath();

			try {
				boolean success = service.downloadFile(file.getName(), localFilePath);

				if (success) {
					JOptionPane.showMessageDialog(null,
							Language.get(107) + localFilePath);
					cn.registerLog(user.getUsernameApp(), "File downloaded:" + localFilePath);
				} else {
					JOptionPane.showMessageDialog(null,
							Language.get(108),
							Language.get(109),
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						Language.get(110) + e.getMessage(),
						Language.get(109),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleDelete(FTPFile file) {
		int confirm = JOptionPane.showConfirmDialog(null,
				Language.get(111) + file.getName() + Language.get(112),
				Language.get(113),
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				boolean success = service.deleteFile(file.getName());

				if (success) {
					JOptionPane.showMessageDialog(null, Language.get(114));

					service.notifyFTPChange("DELETE", file.getName());

					updateTable();
					cn.registerLog(user.getUsernameApp(), "File delete:" + file.getName());
				} else {
					JOptionPane.showMessageDialog(null,
							Language.get(115),
							Language.get(116),
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						Language.get(117) + e.getMessage(),
						Language.get(116),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleRename(FTPFile file) {
		String newName = JOptionPane.showInputDialog(null,
				Language.get(118) + file.getName() + Language.get(119));

		if (newName != null && !newName.trim().isEmpty()) {
			try {
				boolean success = service.renameFile(file.getName(), newName.trim());

				if (success) {
					JOptionPane.showMessageDialog(null, Language.get(120));

					service.notifyFTPChange("RENAME", file.getName() + " -> " + newName.trim());

					updateTable();
					cn.registerLog(user.getUsernameApp(),
							"File renamed :" + file.getName() + " for: " + newName.trim());
				} else {
					JOptionPane.showMessageDialog(null,
							Language.get(121),
							Language.get(122),
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						Language.get(123) + e.getMessage(),
						Language.get(122),
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

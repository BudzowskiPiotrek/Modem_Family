package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;

public class FTPbtnNewFolder implements ActionListener {

	private final JButton button;
	private final ServiceFTP service;
	private final FTPTableModel model;
	private final Usuario user;
	private final Common cn = new Common();

	public FTPbtnNewFolder(JButton button, ServiceFTP service, FTPTableModel model, Usuario user) {
		this.user = user;
		this.button = button;
		this.service = service;
		this.model = model;
		this.button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String folderName = JOptionPane.showInputDialog(null, "Name of the new folder:", "Create Folder",
				JOptionPane.QUESTION_MESSAGE);

		if (folderName == null || folderName.trim().isEmpty()) {
			return;
		}

		folderName = folderName.trim();

		try {
			boolean created = service.makeDirectory(folderName);

			if (created) {
				model.setData(Arrays.asList(service.listAllFiles()));
				cn.registerLog(user.getUsernameApp(), "New Folder: " + folderName);
				service.notifyFTPChange("Create folder", folderName);
			}

		} catch (IOException ex) {

			if ("DIRECTORY_EXISTS".equals(ex.getMessage())) {
				JOptionPane.showMessageDialog(null, "The folder already exists.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "The folder could not be created.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}


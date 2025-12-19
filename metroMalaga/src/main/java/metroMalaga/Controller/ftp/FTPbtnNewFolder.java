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
import metroMalaga.Model.Language;

public class FTPbtnNewFolder implements ActionListener {

	private final JButton button;
	private final ServiceFTP service;
	private final FTPTableModel model;
	private final Usuario user;
	private final Common cn = new Common();

	/**
	 * Constructor for FTPbtnNewFolder class.
	 * 
	 * @param button  The JButton component.
	 * @param service The FTP service instance.
	 * @param model   The FTP table model.
	 * @param user    The current user.
	 */
	public FTPbtnNewFolder(JButton button, ServiceFTP service, FTPTableModel model, Usuario user) {
		this.user = user;
		this.button = button;
		this.service = service;
		this.model = model;
		this.button.addActionListener(this);
	}

	/**
	 * Handles the action to create a new folder.
	 * 
	 * @param e The ActionEvent.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		String folderName = JOptionPane.showInputDialog(null, Language.get(132), Language.get(133),
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
				JOptionPane.showMessageDialog(null, Language.get(134), Language.get(102), JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, Language.get(135), Language.get(101), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

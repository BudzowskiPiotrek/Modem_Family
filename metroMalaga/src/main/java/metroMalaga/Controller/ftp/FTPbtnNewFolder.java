package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;

import java.util.Arrays;

public class FTPbtnNewFolder implements ActionListener {
	private JButton button;
	private ServiceFTP service;
	private FTPTableModel model;
	private Common cn = new Common();
	private Usuario user;

	public FTPbtnNewFolder(JButton button, ServiceFTP service, FTPTableModel model, Usuario user) {
		this.user = user;
		this.button = button;
		this.service = service;
		this.model = model;
		this.button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String folderName = JOptionPane.showInputDialog(null, "Enter folder name:", "New Folder",
				JOptionPane.QUESTION_MESSAGE);

		if (folderName != null && !folderName.trim().isEmpty()) {
			boolean success = service.makeDirectory(folderName.trim());

			if (success) {
				model.setData(Arrays.asList(service.listAllFiles()));
				cn.registerLog(user.getUsernameApp(), "Create folder:" + folderName.trim());
			} else {
				JOptionPane.showMessageDialog(null, "Could not create folder.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
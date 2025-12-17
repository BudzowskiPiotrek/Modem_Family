package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;

public class FTPbtnNewFolder implements ActionListener {
	private JButton button;
	private ServiceFTP service;
	private FTPTableModel model;
	private Usuario user;
	private Common cn = new Common();

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

		if (folderName != null && !folderName.trim().isEmpty()) {
			boolean exito = service.makeDirectory(folderName.trim());

			if (exito) {
				model.setData(Arrays.asList(service.listAllFiles()));
				cn.registerLog(user.getUsernameApp(), "New Folder: " + folderName.trim());
			} else {
				JOptionPane.showMessageDialog(null, "The folder could not be created.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
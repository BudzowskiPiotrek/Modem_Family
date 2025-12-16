package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;

public class FTPbtnUpFile implements ActionListener {

	private ServiceFTP service;
	private FTPTableModel model;
	private Common cn;
	private Usuario user;

	public FTPbtnUpFile(JButton button, ServiceFTP service, FTPTableModel model, Usuario user) {
		this.cn = new Common();
		this.user = user;
		this.service = service;
		this.model = model;
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select file to upload");

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			File localFile = fileChooser.getSelectedFile();

			String remoteFileName = localFile.getName();

			try {
				boolean success = service.uploadFile(localFile.getAbsolutePath(), remoteFileName);

				if (success) {

					JOptionPane.showMessageDialog(null, "File uploaded successfully:" + remoteFileName);
					
					cn.registerLog(user.getUsernameApp(), "File uploaded:" + remoteFileName);
					FTPFile[] updatedFilesArray = service.listAllFiles();
					List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));
					model.setData(updatedFilesList);

				} else {
					JOptionPane.showMessageDialog(null, "File upload failed.", "Upload Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "I/O error during upload: " + ex.getMessage(), "Error de Subida",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

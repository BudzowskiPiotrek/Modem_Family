package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;

public class FTPbtnUp implements ActionListener {

	private final ServiceFTP service;
	private final FTPTableModel model;

	public FTPbtnUp(JButton button, ServiceFTP service, FTPTableModel model) {
		this.service = service;
		this.model = model;
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			boolean success = service.changeDirectoryUp();

			if (success) {
				FTPFile[] updatedFilesArray = service.listAllFiles();
				List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));

				model.setData(updatedFilesList);

			} else {
				JOptionPane.showMessageDialog(null, "You are already in the root directory or the change failed.",
						"Error de Navegación", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error trying to return:" + ex.getMessage(), "FTP Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
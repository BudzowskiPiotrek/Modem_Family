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
import metroMalaga.Model.Language;

public class FTPbtnUp implements ActionListener {

	private final ServiceFTP service;
	private final FTPTableModel model;

	/**
	 * Constructor for FTPbtnUp class.
	 * 
	 * @param button  The JButton component.
	 * @param service The FTP service instance.
	 * @param model   The FTP table model.
	 */
	public FTPbtnUp(JButton button, ServiceFTP service, FTPTableModel model) {
		this.service = service;
		this.model = model;
		button.addActionListener(this);
	}

	/**
	 * Handles the action to move up one directory level.
	 * 
	 * @param e The ActionEvent.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			boolean success = service.changeDirectoryUp();

			if (success) {
				FTPFile[] updatedFilesArray = service.listAllFiles();
				List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));

				model.setData(updatedFilesList);

			} else {
				JOptionPane.showMessageDialog(null,
						Language.get(129),
						Language.get(130),
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Language.get(131) + ex.getMessage(),
					Language.get(101),
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

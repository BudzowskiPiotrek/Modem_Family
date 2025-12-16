package metroMalaga.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Model.FTPTableModel;

public class FTPbtnRefresh implements ActionListener {

	private final JButton refreshButton;
	private final ServiceFTP service;
	private final FTPTableModel ftpModel;

	public FTPbtnRefresh(JButton refreshButton, ServiceFTP service, FTPTableModel ftpModel) {
		this.refreshButton = refreshButton;
		this.service = service;
		this.ftpModel = ftpModel;
		refreshButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			FTPFile[] fileArray = service.listAllFiles();
			List<FTPFile> newFiles = new ArrayList<>(Arrays.asList(fileArray));

			ftpModel.setData(newFiles);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error refreshing the file list: " + ex.getMessage(), "FTP Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

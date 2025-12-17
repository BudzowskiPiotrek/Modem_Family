package metroMalaga.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.Server.NotificationController;
import metroMalaga.Controller.Server.NotificationServer;
import metroMalaga.Model.FTPTableModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ServiceFTP {

	private FTPClient ftpClient;
	private ConnecionFTP conFTP;
	private String user;

	// ????
	private NotificationController notificationController;
	private boolean notificationsInitialized = false;
	private String ftpServerHost;
	private static final String NOTIFICATION_SERVER_HOST = "127.0.0.1";

	public ServiceFTP(String user) {
		this.user = user;
		this.conFTP = new ConnecionFTP(user);
		this.ftpClient = conFTP.getConnection();

		if (ftpClient == null) {
			JOptionPane.showMessageDialog(null, "FTP connection could not be established.", "FTP Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			this.ftpServerHost = conFTP.getServerHost();
		} catch (IOException e) {
			showError("Error configuring FTP connection", e);
		}
		initializeNotificationSystem();
	}

	public FTPFile[] listAllFiles() {
		try {
			return ftpClient.listFiles();
		} catch (IOException e) {
			showError("Could not retrieve file list from server", e);
			return new FTPFile[0];
		}
	}

	public boolean changeDirectory(String directoryName) {
		try {
			return ftpClient.changeWorkingDirectory(directoryName);
		} catch (IOException e) {
			showError("Could not change directory to: " + directoryName, e);
			return false;
		}
	}

	public boolean changeDirectoryUp() {
		try {
			return ftpClient.changeWorkingDirectory("..");
		} catch (IOException e) {
			showError("Could not move to parent directory", e);
			return false;
		}
	}

	public String getCurrentDirectory() {
		try {
			return ftpClient.printWorkingDirectory();
		} catch (IOException e) {
			showError("Could not retrieve current directory", e);
			return "";
		}
	}

	public boolean uploadFile(String localFilePath, String remoteFilePath) {
		File localFile = new File(localFilePath);

		if (!localFile.exists()) {
			JOptionPane.showMessageDialog(null, "Local file does not exist:\n" + localFilePath, "Upload Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try (InputStream inputStream = new FileInputStream(localFile)) {
			return ftpClient.storeFile(remoteFilePath, inputStream);
		} catch (IOException e) {
			showError("Error uploading file: " + remoteFilePath, e);
			return false;
		}
	}

	public boolean downloadFile(String remoteFilePath, String localFilePath) {
		try (OutputStream outputStream = new FileOutputStream(localFilePath)) {
			return ftpClient.retrieveFile(remoteFilePath, outputStream);
		} catch (IOException e) {
			showError("Error downloading file: " + remoteFilePath, e);
			return false;
		}
	}

	public boolean deleteFile(String remoteFilePath) {
		try {
			return ftpClient.deleteFile(remoteFilePath);
		} catch (IOException e) {
			showError("Could not delete file: " + remoteFilePath, e);
			return false;
		}
	}

	public boolean renameFile(String remoteFrom, String remoteTo) {
		try {
			return ftpClient.rename(remoteFrom, remoteTo);
		} catch (IOException e) {
			showError("Could not rename file", e);
			return false;
		}
	}

	public void close() {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				System.err.println("Error closing FTP connection: " + e.getMessage());
			}
		}
	}

	public long calculateDirectorySize(String directoryName) {
		long totalSize = 0;
		String originalDirectory = getCurrentDirectory();
		try {
			if (ftpClient.changeWorkingDirectory(directoryName)) {
				FTPFile[] files = ftpClient.listFiles();
				for (FTPFile file : files) {
					if (file.getName().equals(".") || file.getName().equals("..")) {
						continue;
					}
					if (file.isFile()) {
						totalSize += file.getSize();
					} else if (file.isDirectory()) {
						totalSize += calculateDirectorySize(file.getName());
					}
				}
				ftpClient.changeWorkingDirectory("..");

			}
		} catch (IOException e) {
			showError("Error calculando tamaño recursivo para: " + directoryName, e);
		}
		try {
			ftpClient.changeWorkingDirectory(originalDirectory);
		} catch (IOException e) {
			showError("Error al regresar al directorio original.", e);
		}

		return totalSize;
	}

	private void showError(String message, Exception e) {
		JOptionPane.showMessageDialog(null, message + "\nDetails: " + e.getMessage(), "FTP Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Initialize notification system: start server if we're on the server host,
	 * otherwise connect as client to the server
	 */
	private void initializeNotificationSystem() {
		if (NOTIFICATION_SERVER_HOST.equals(ftpServerHost)) {
			// We are on the notification server host, start the server
			NotificationServer.getInstance().start();
			System.out.println("Running as NOTIFICATION SERVER (FTP server at " + ftpServerHost + ")");
		} else {
			// We are a remote client, will connect to notification server when table model
			// is set
			System.out.println("Running as NOTIFICATION CLIENT (will connect to server at " + ftpServerHost + ")");
		}
	}

	/**
	 * Set the table model and initialize notifications if not already initialized.
	 * This must be called after creating the FTPTableModel.
	 * 
	 * @param tableModel The FTP table model to update when notifications are
	 *                   received
	 */
	public void setTableModel(FTPTableModel tableModel) {
		if (tableModel != null) {
			// Only create notification controller (client) if we're NOT running as server
			this.notificationController = new NotificationController(tableModel, this, ftpServerHost);
			notificationsInitialized = true;
		}
	}

	/**
	 * Disconnect from the notification system
	 */
	public void disconnectNotifications() {
		if (notificationController != null) {
			notificationController.disconnect();
		}
	}

	/**
	 * Set the notification controller for broadcasting FTP changes
	 * 
	 * @param controller The notification controller
	 */
	public void setNotificationController(NotificationController controller) {
		this.notificationController = controller;
	}
}

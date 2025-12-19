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
	private String currentUsername; // Username of the logged-in user
	private metroMalaga.Model.Rol userRole; // Role object of the logged-in user
	private NotificationController notificationController;
	private boolean notificationsInitialized = false;
	private String ftpServerHost;
	private static final String NOTIFICATION_SERVER_HOST = "127.0.0.1";

	/**
	 * Constructor for ServiceFTP
	 * 
	 * @param username Username of the logged-in user
	 * @param rol      Role object of the user
	 */
	public ServiceFTP(String username, metroMalaga.Model.Rol rol) {
		this.currentUsername = username;
		this.userRole = rol;
		this.conFTP = new ConnecionFTP();
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

	/**
	 * Creates a new directory on the FTP server.
	 * 
	 * @param path The path of the directory to be created.
	 * @return true if the directory was created successfully, false otherwise.
	 * @throws IOException If an I/O error occurs or the directory already exists.
	 */
	public synchronized boolean makeDirectory(String path) throws IOException {

		if (!isConnected()) {
			reconnect();
		}

		boolean created = ftpClient.makeDirectory(path);

		// Register folder ownership if creation was successful
		if (created && currentUsername != null) {
			metroMalaga.Model.FTPFileOwnershipDAO.registerFile(path, currentUsername, true);
		}

		if (!created) {
			int replyCode = ftpClient.getReplyCode();

			if (replyCode == 550) {
				throw new IOException("DIRECTORY_EXISTS");
			} else {
				throw new IOException("FTP_ERROR");
			}
		}

		return true;
	}

	/**
	 * Lists all files in the current working directory.
	 * 
	 * @return An array of FTPFile objects representing the files in the directory.
	 */
	public synchronized FTPFile[] listAllFiles() {
		try {
			if (!isConnected()) {
				System.out.println("FTP connection lost, attempting to reconnect...");
				reconnect();
			}

			return ftpClient.listFiles();
		} catch (IOException e) {
			System.err.println("Error listing files, attempting reconnect: " + e.getMessage());
			try {
				reconnect();
				return ftpClient.listFiles();
			} catch (IOException retryEx) {
				showError("Could not retrieve file list from server", retryEx);
				return new FTPFile[0];
			}
		}
	}

	/**
	 * Check if FTP connection is still alive
	 */
	private boolean isConnected() {
		try {
			return ftpClient != null && ftpClient.isConnected() && ftpClient.sendNoOp();
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Reconnect to FTP server
	 */
	private void reconnect() throws IOException {
		System.out.println("Reconnecting to FTP server...");
		// Close old connection if exists
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				// Ignore errors when disconnecting
			}
		}

		// Create new connection
		this.ftpClient = conFTP.getConnection();
		if (ftpClient == null) {
			throw new IOException("Could not reconnect to FTP server");
		}

		try {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			System.out.println("Successfully reconnected to FTP server");
		} catch (IOException e) {
			throw new IOException("Error configuring reconnected FTP connection: " + e.getMessage());
		}
	}

	/**
	 * Changes the current working directory on the FTP server.
	 * 
	 * @param directoryName The name of the directory to change to.
	 * @return true if the directory change was successful, false otherwise.
	 */
	public synchronized boolean changeDirectory(String directoryName) {
		try {
			return ftpClient.changeWorkingDirectory(directoryName);
		} catch (IOException e) {
			showError("Could not change directory to: " + directoryName, e);
			return false;
		}
	}

	/**
	 * Changes the working directory to the parent directory.
	 * 
	 * @return true if the directory change was successful, false otherwise.
	 */
	public boolean changeDirectoryUp() {
		try {
			return ftpClient.changeWorkingDirectory("..");
		} catch (IOException e) {
			showError("Could not move to parent directory", e);
			return false;
		}
	}

	/**
	 * Retrieves the current working directory path.
	 * 
	 * @return The current working directory path as a String.
	 */
	public String getCurrentDirectory() {
		try {
			return ftpClient.printWorkingDirectory();
		} catch (IOException e) {
			showError("Could not retrieve current directory", e);
			return "";
		}
	}

	/**
	 * Uploads a local file to the FTP server.
	 * 
	 * @param localFilePath  The path of the local file to upload.
	 * @param remoteFilePath The path on the server where the file should be stored.
	 * @return true if the upload was successful, false otherwise.
	 */
	public synchronized boolean uploadFile(String localFilePath, String remoteFilePath) {
		File localFile = new File(localFilePath);

		if (!localFile.exists()) {
			JOptionPane.showMessageDialog(null, "Local file does not exist:\n" + localFilePath, "Upload Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try (InputStream inputStream = new FileInputStream(localFile)) {
			boolean success = ftpClient.storeFile(remoteFilePath, inputStream);

			System.out.println("DEBUG uploadFile - Upload success: " + success);
			System.out.println("DEBUG uploadFile - currentUsername: " + currentUsername);
			System.out.println("DEBUG uploadFile - remoteFilePath: " + remoteFilePath);

			// Register file ownership if upload was successful
			if (success && currentUsername != null) {
				System.out.println("DEBUG uploadFile - Registering ownership...");
				metroMalaga.Model.FTPFileOwnershipDAO.registerFile(remoteFilePath, currentUsername, false);
				System.out.println("DEBUG uploadFile - Ownership registered!");
			} else {
				System.out.println("DEBUG uploadFile - NOT registering ownership. success=" + success + ", username="
						+ currentUsername);
			}

			return success;
		} catch (IOException e) {
			showError("Error uploading file: " + remoteFilePath, e);
			return false;
		}
	}

	/**
	 * Downloads a file from the FTP server to the local machine.
	 * 
	 * @param remoteFilePath The path of the file on the FTP server.
	 * @param localFilePath  The path on the local machine where the file should be
	 *                       saved.
	 * @return true if the download was successful, false otherwise.
	 */
	public synchronized boolean downloadFile(String remoteFilePath, String localFilePath) {
		try (OutputStream outputStream = new FileOutputStream(localFilePath)) {
			return ftpClient.retrieveFile(remoteFilePath, outputStream);
		} catch (IOException e) {
			showError("Error downloading file: " + remoteFilePath, e);
			return false;
		}
	}

	/**
	 * Deletes a file or directory on the FTP server.
	 * 
	 * @param path The path of the file or directory to delete.
	 * @return true if the deletion was successful, false otherwise.
	 */
	public synchronized boolean deleteFile(String path) {
		// Check if user has permission to delete this file
		if (currentUsername != null && userRole != null) {
			if (!metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(path, currentUsername, userRole, true)) {
				JOptionPane.showMessageDialog(null,
						"No tienes permisos para borrar este archivo.\nSolo puedes borrar tus propios archivos.",
						"Permiso Denegado", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		try {
			FTPFile file = ftpClient.mlistFile(path);

			if (file == null) {
				return false;
			}

			if (file.isDirectory()) {
				deleteDirectoryRecursive(path);
			} else {
				ftpClient.deleteFile(path);
			}

			// Remove from ownership database after successful deletion
			metroMalaga.Model.FTPFileOwnershipDAO.deleteFile(path);

			return true;
		} catch (IOException e) {
			showError("Could not delete: " + path, e);
			return false;
		}
	}

	/**
	 * Recursively deletes a directory and its contents.
	 * 
	 * @param dirPath The path of the directory to delete.
	 * @throws IOException If an I/O error occurs during deletion.
	 */
	private void deleteDirectoryRecursive(String dirPath) throws IOException {
		FTPFile[] files = ftpClient.listFiles(dirPath);
		for (FTPFile file : files) {
			String fullPath = dirPath + "/" + file.getName();
			if (file.isDirectory()) {
				deleteDirectoryRecursive(fullPath);
			} else {
				ftpClient.deleteFile(fullPath);
				// Delete ownership record for each file
				metroMalaga.Model.FTPFileOwnershipDAO.deleteFile(fullPath);
			}
		}
		ftpClient.removeDirectory(dirPath);
		// Delete ownership record for the directory itself
		metroMalaga.Model.FTPFileOwnershipDAO.deleteFile(dirPath);
	}

	/**
	 * Renames a file or directory on the FTP server.
	 * 
	 * @param remoteFrom The current name/path of the file.
	 * @param remoteTo   The new name/path for the file.
	 * @return true if the rename was successful, false otherwise.
	 */
	public synchronized boolean renameFile(String remoteFrom, String remoteTo) {
		// Check if user has permission to rename this file
		if (currentUsername != null && userRole != null) {
			if (!metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(remoteFrom, currentUsername, userRole, false)) {
				JOptionPane.showMessageDialog(null,
						"No tienes permisos para renombrar este archivo.\nSolo puedes renombrar tus propios archivos.",
						"Permiso Denegado", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		try {
			boolean success = ftpClient.rename(remoteFrom, remoteTo);

			// Update ownership database if rename was successful
			if (success) {
				metroMalaga.Model.FTPFileOwnershipDAO.renameFile(remoteFrom, remoteTo);
			}

			return success;
		} catch (IOException e) {
			showError("Could not rename file", e);
			return false;
		}
	}

	/**
	 * Closes the FTP connection and logs out.
	 */
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

	/**
	 * Calculates the total size of a directory recursively.
	 * 
	 * @param directoryName The name of the directory to calculate.
	 * @return The total size in bytes.
	 */
	public synchronized long calculateDirectorySize(String directoryName) {
		long totalSize = 0;
		String originalDirectory = getCurrentDirectory();

		try {
			// Check if connection is still alive, reconnect if needed
			if (!isConnected()) {
				System.out.println("FTP connection lost, attempting to reconnect...");
				reconnect();
			}

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
			// Try to reconnect and retry
			System.err.println("Error calculating directory size, attempting reconnect: " + e.getMessage());
			try {
				reconnect();
				// Don't retry the whole operation, just return 0 for this directory
				// to avoid infinite recursion if reconnection keeps failing
			} catch (IOException reconnectEx) {
				System.err.println("Could not reconnect: " + reconnectEx.getMessage());
			}
		}

		try {
			ftpClient.changeWorkingDirectory(originalDirectory);
		} catch (IOException e) {
			System.err.println("Error al regresar al directorio original: " + e.getMessage());
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
		try {
			if (NOTIFICATION_SERVER_HOST.equals(ftpServerHost)) {
				// We are on the notification server host, start the server
				NotificationServer.getInstance().start();
				System.out.println("Running as NOTIFICATION SERVER (FTP server at " + ftpServerHost + ")");
			} else {
				// We are a remote client, will connect to notification server when table model
				// is set
				System.out.println("Running as NOTIFICATION CLIENT (will connect to server at " + ftpServerHost + ")");
			}
		} catch (Exception e) {
			// If notification system fails, log it but don't prevent FTP from working
			System.err.println("Warning: Notification system initialization failed: " + e.getMessage());
			System.out.println("FTP will work normally, but without real-time synchronization");
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
			try {
				// Try to create notification controller (client)
				this.notificationController = new NotificationController(tableModel, this, ftpServerHost);
				notificationsInitialized = true;
				System.out.println("Notification system initialized successfully");
			} catch (Exception e) {
				// If notification system fails to initialize, log it but DON'T block FTP access
				System.err.println("Warning: Notification system could not be initialized: " + e.getMessage());
				System.out.println("FTP will work normally, but without real-time updates from other clients");
				this.notificationController = null;
				notificationsInitialized = false;
			}
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
	 * Notify all clients about an FTP change This method should be called after
	 * successful FTP operations (upload, delete, rename, etc.)
	 * 
	 * @param action   The type of action (UPLOAD, DELETE, RENAME, etc.)
	 * @param filePath The affected file path
	 */
	public void notifyFTPChange(String action, String filePath) {
		try {
			if (notificationController != null) {
				notificationController.notifyChange(action, filePath);
			} else {
				System.out.println("Notification controller not initialized - notification not sent for: " + action
						+ " " + filePath);
			}
		} catch (Exception e) {
			// Don't let notification errors break the FTP operation
			System.err.println("Error sending notification: " + e.getMessage());
			e.printStackTrace();
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

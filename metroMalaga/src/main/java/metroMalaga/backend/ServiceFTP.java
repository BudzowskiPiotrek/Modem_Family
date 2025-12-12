package metroMalaga.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ServiceFTP {
	private String user;
	private ConnecionFTP conFTP;

	public ServiceFTP(String user) {
		this.user = user;
		this.conFTP = new ConnecionFTP(user);
	}

	public FTPFile[] listAllFiles() {
		FTPClient ftpClient = null;

		try {
			ftpClient = conFTP.getConnection();
			if (ftpClient == null) {
				return new FTPFile[0];
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			FTPFile[] files = ftpClient.listFiles();

			return files;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"FTP Error: Could not retrieve file list from server. Details: " + e.getMessage(),
					"File Listing Error", JOptionPane.ERROR_MESSAGE);
			return new FTPFile[0];
		} finally {
			conFTP.closeConnection(ftpClient);
		}
	}

	public List<FTPFile> searchFiles(String searchText) {
		FTPFile[] allFiles = listAllFiles();
		List<FTPFile> foundFiles = new ArrayList<>();
		if (allFiles.length == 0) {
			return foundFiles;
		}

		String lowerSearchText = searchText.toLowerCase();

		for (FTPFile file : allFiles) {
			if (file.getName().toLowerCase().contains(lowerSearchText) && file.isValid()) {
				foundFiles.add(file);
			}
		}
		return foundFiles;
	}

	// PRIMEROES RUTA COMPLETA DE ARVHIVO AL SUBIR Y RUTA EN UNIDAD DONDE LO VAMOS
	// GUARDAR EJEMPLO CARPETA/IMAGEN.JPG
	public boolean uploadFile(String localFilePath, String remoteFilePath) {
		FTPClient ftpClient = null;
		InputStream inputStream = null;
		boolean success = false;

		try {
			ftpClient = conFTP.getConnection();
			if (ftpClient == null) {
				return false;
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			File localFile = new File(localFilePath);

			if (!localFile.exists()) {
				JOptionPane.showMessageDialog(null,
						"Local File Error: The file specified at " + localFilePath + " does not exist.", "Upload Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			inputStream = new FileInputStream(localFile);
			success = ftpClient.storeFile(remoteFilePath, inputStream);
			if (!success) {
				JOptionPane.showMessageDialog(null,
						"Upload Failed: The file upload failed. Server response code: " + ftpClient.getReplyCode()
								+ ". Check if the user has WRITE permissions.",
						"Upload Failed", JOptionPane.ERROR_MESSAGE);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"File Transfer Error: An error occurred during file upload. Details: " + e.getMessage(),
					"File Transfer Error", JOptionPane.ERROR_MESSAGE);
			success = false;

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing input stream: " + e.getMessage());
				}
			}
			conFTP.closeConnection(ftpClient);
		}
		return success;
	}

	public boolean downloadFile(String remoteFilePath, String localFilePath) {
		FTPClient ftpClient = null;
		OutputStream outputStream = null;
		boolean success = false;

		try {
			ftpClient = conFTP.getConnection();
			if (ftpClient == null) {
				return false;
			}

			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			outputStream = new FileOutputStream(localFilePath);
			success = ftpClient.retrieveFile(remoteFilePath, outputStream);

			if (!success) {
				JOptionPane.showMessageDialog(null,
						"Download Failed: The file '" + remoteFilePath
								+ "' was not found on the server or access was denied. Server response code: "
								+ ftpClient.getReplyCode(),
						"Download Failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"File Transfer Error: An error occurred during file download. Details: " + e.getMessage(),
					"File Transfer Error", JOptionPane.ERROR_MESSAGE);
			success = false;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.err.println("Error closing output stream: " + e.getMessage());
				}
			}
			conFTP.closeConnection(ftpClient);
		}
		return success;
	}

	public boolean deleteFile(String remoteFilePath) {
		FTPClient ftpClient = null;
		boolean success = false;

		try {
			ftpClient = conFTP.getConnection();
			if (ftpClient == null) {
				return false;
			}

			success = ftpClient.deleteFile(remoteFilePath);

			if (!success) {
				JOptionPane.showMessageDialog(null,
						"Deletion Failed: Could not delete the file '" + remoteFilePath
								+ "'. Check server permissions or if the file exists. Server response: "
								+ ftpClient.getReplyString(),
						"Deletion Failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"FTP Error: An error occurred during file deletion. Details: " + e.getMessage(),
					"FTP Deletion Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			conFTP.closeConnection(ftpClient);
		}
		return success;
	}

	// SE PUEDE UTILIZARLO TAMBIEN DE MOVERLO DE SITIO NO SOLO DE RENOMBRAR
	public boolean renameFile(String remotePathFrom, String remotePathTo) {
		FTPClient ftpClient = null;
		boolean success = false;

		try {
			ftpClient = conFTP.getConnection();
			if (ftpClient == null) {
				return false;
			}

			success = ftpClient.rename(remotePathFrom, remotePathTo);

			if (!success) {
				JOptionPane.showMessageDialog(null,
						"Rename Failed: Could not rename '" + remotePathFrom
								+ "'. Check server permissions. Server response: " + ftpClient.getReplyString(),
						"Rename Failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"FTP Error: An error occurred during file rename. Details: " + e.getMessage(), "FTP Rename Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			conFTP.closeConnection(ftpClient);
		}
		return success;
	}
}

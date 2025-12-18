package metroMalaga.Controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.FTPClient;

public class ConnecionFTP {
	private static final String SERVER = "192.168.1.35";
	private static final int PORT = 21;
	private String user;
	private static final String PASS = "proyecto";

	public ConnecionFTP(String tipo) {
		if (tipo.equalsIgnoreCase("admin")) {
			this.user = "proyecto";
		} else if (tipo.equalsIgnoreCase("LECTURA")) {
			this.user = "read";
		}
	}

	public FTPClient getConnection() {
		FTPClient ftpClient = new FTPClient();
		try {
			// Configure timeouts before connecting (these methods only accept int
			// milliseconds)
			ftpClient.setDefaultTimeout(60000); // 60 seconds for connection timeout
			ftpClient.setConnectTimeout(60000); // 60 seconds for socket connection

			// Set keep-alive to prevent connection from closing during idle time
			ftpClient.setControlKeepAliveTimeout(java.time.Duration.ofMinutes(5)); // Send NOOP every 5 minutes

			ftpClient.connect(SERVER, PORT);
			boolean login = ftpClient.login(user, PASS);
			if (login) {
				ftpClient.enterLocalPassiveMode();

				// Set data timeout and keep-alive reply timeout after connection is established
				ftpClient.setDataTimeout(java.time.Duration.ofSeconds(60)); // 60 seconds for data transfer
				ftpClient.setControlKeepAliveReplyTimeout(java.time.Duration.ofSeconds(60)); // 60 seconds timeout for
																								// keep-alive responses

				System.out.println("FTP connection established with keep-alive enabled (NOOP every 5 minutes)");
				return ftpClient;
			} else {
				closeConnection(ftpClient);
				JOptionPane.showMessageDialog(null,
						"Login Failed for user: " + user
								+ ". Check password in ConnecionFTP.java and user privileges on FileZilla Server.",
						"Authentication Error", JOptionPane.ERROR_MESSAGE);

			}
		} catch (IOException e) {
			String errorMessage = "Could not connect to the FTP Server at " + SERVER + ":" + PORT
					+ ". The server may be offline or the firewall is blocking access." + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Error FTP", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void closeConnection(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				String errorMessage = "Error while disconnecting FTP: " + e.getMessage();
				JOptionPane.showMessageDialog(null, errorMessage, "Error FTP", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public String getServerHost() {
		return SERVER;
	}
}

package metroMalaga.Controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.FTPClient;
import metroMalaga.Model.Language;

public class ConnecionFTP {
	private static final String SERVER = "192.168.1.35";
	private static final int PORT = 21;
	private String user = "proyecto";
	private static final String PASS = "proyecto";

	public ConnecionFTP() {
	}

	public FTPClient getConnection() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setDefaultTimeout(60000);
			ftpClient.setConnectTimeout(60000);

			ftpClient.setControlKeepAliveTimeout(java.time.Duration.ofMinutes(5));

			ftpClient.connect(SERVER, PORT);
			boolean login = ftpClient.login(user, PASS);
			if (login) {
				ftpClient.enterLocalPassiveMode();

				ftpClient.setDataTimeout(java.time.Duration.ofSeconds(60));
				ftpClient.setControlKeepAliveReplyTimeout(java.time.Duration.ofSeconds(60));

				System.out.println("FTP connection established with keep-alive enabled (NOOP every 5 minutes)");
				return ftpClient;
			} else {
				closeConnection(ftpClient);
				JOptionPane.showMessageDialog(null, Language.get(189) + user + Language.get(190), Language.get(147),
						JOptionPane.ERROR_MESSAGE);

			}
		} catch (IOException e) {
			String errorMessage = Language.get(191) + SERVER + ":" + PORT + Language.get(192) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(193), JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void closeConnection(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				String errorMessage = Language.get(194) + e.getMessage();
				JOptionPane.showMessageDialog(null, errorMessage, Language.get(193), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public String getServerHost() {
		return SERVER;
	}
}

package metroMalaga.backend;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class ConnecionFTP {
	private final String SERVER = "127.0.0.1";
	private final int PORT = 21;
	private final String USER = "test_user";
	private final String PASS = "password123";
	
	public FTPClient connect() {
        FTPClient ftpClient = new FTPClient();
        boolean loggedIn = false;
        
        try {
            ftpClient.connect(SERVER, PORT);
            if (!ftpClient.isConnected()) {
                System.err.println("ERROR FTP: Conexión de red fallida.");
                return null;
            }
            ftpClient.enterLocalPassiveMode();

            loggedIn = ftpClient.login(USER, PASS);
            
            if (loggedIn) {

                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                System.out.println("Conexión FTP y login exitosos.");
                return ftpClient;
            } else {
                System.err.println("ERROR FTP: Login fallido. Revise credenciales. Código: " + ftpClient.getReplyCode());
                ftpClient.disconnect();
                return null;
            }

        } catch (IOException ex) {
            System.err.println("ERROR FTP: Fallo de I/O o red. Detalle: " + ex.getMessage());
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
            return null;
        }
    }
}

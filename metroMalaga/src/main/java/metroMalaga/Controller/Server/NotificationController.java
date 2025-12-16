package metroMalaga.Controller.Server;

import javax.swing.SwingUtilities;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;

/**
 * Controller that coordinates FTP notifications between clients and manages
 * table updates.
 */
public class NotificationController implements NotificationClient.MessageListener {
    private NotificationClient client;
    private FTPTableModel tableModel;
    private ServiceFTP service;
    private String serverHost;

    public NotificationController(FTPTableModel tableModel, ServiceFTP service, String serverHost) {
        this.tableModel = tableModel;
        this.service = service;
        this.serverHost = serverHost;
        this.client = new NotificationClient();

        // Connect to notification server
        if (client.connect(serverHost)) {
            client.setMessageListener(this);
        } else {
            System.err.println("Warning: Could not connect to notification server at " + serverHost
                    + ". Live updates will not work.");
        }
    }

    /**
     * Called when a notification message is received from the server
     */
    @Override
    public void onMessageReceived(String message) {
        // Use SwingUtilities to update the UI from the notification thread
        SwingUtilities.invokeLater(() -> {
            System.out.println("Processing notification: " + message);
            refreshTable();
        });
    }

    /**
     * Notify all clients about an FTP change
     * 
     * @param action   The type of action (UPLOAD, DELETE, RENAME, etc.)
     * @param filePath The affected file path
     */
    public void notifyChange(String action, String filePath) {
        String message = action + ":" + filePath;

        // Send notification to server to broadcast to all clients
        NotificationServer.getInstance().broadcast(message);

        System.out.println("Notified change: " + message);
    }

    /**
     * Refresh the FTP table with current data from server
     */
    private void refreshTable() {
        try {
            // Reload file list from FTP server
            java.util.List<org.apache.commons.net.ftp.FTPFile> files = java.util.Arrays.asList(service.listAllFiles());

            // Update table model
            tableModel.setData(files);

            System.out.println("Table refreshed with " + files.size() + " files");
        } catch (Exception e) {
            System.err.println("Error refreshing table: " + e.getMessage());
        }
    }

    /**
     * Disconnect from notification server
     */
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
}

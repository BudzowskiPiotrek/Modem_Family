package metroMalaga.Controller.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;

/**
 * Controller that coordinates FTP notifications between clients and manages
 * table updates. Acts as an adapter between the NotificationClient and the UI
 * model.
 */
public class NotificationController implements NotificationClient.MessageListener {
    private NotificationClient client;
    private FTPTableModel tableModel;
    private ServiceFTP service;
    private String serverHost;

    /**
     * Constructor for NotificationController.
     * 
     * @param tableModel The FTP table model to update.
     * @param service    The FTP service for refreshing data.
     * @param serverHost The hostname of the notification server.
     */
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
     * Called when a notification message is received from the server.
     * Triggers a UI refresh.
     * 
     * @param message The received message.
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
     * Notify all clients about an FTP change via the server.
     * 
     * @param action   The type of action (UPLOAD, DELETE, RENAME, etc.)
     * @param filePath The affected file path
     */
    public void notifyChange(String action, String filePath) {
        String message = action + ":" + filePath;

        // Send notification to server through socket (works for distributed
        // architecture)
        if (client != null && client.isConnected()) {
            if (client.sendMessage(message)) {
                System.out.println("Notified server about change: " + message);
            } else {
                System.err.println("Failed to notify server about change: " + message);
            }
        } else {
            System.err.println("Cannot notify change - client not connected to server");
        }
    }

    /**
     * Refresh the FTP table with current data from server.
     */
    private void refreshTable() {
        try {
            System.out.println("Refreshing FTP table...");
            FTPFile[] updatedFilesArray = service.listAllFiles();

            if (updatedFilesArray != null && updatedFilesArray.length >= 0) {
                List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));
                tableModel.setData(updatedFilesList);
                System.out.println("Table refreshed successfully with " + updatedFilesArray.length + " files");
            } else {
                System.err.println("Failed to retrieve file list - received null or empty array");
            }
        } catch (Exception e) {
            System.err.println("Error refreshing table: " + e.getMessage());
            e.printStackTrace();
            // Don't crash the application, just log the error
        }
    }

    /**
     * Disconnect from notification server.
     */
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
}

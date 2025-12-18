package metroMalaga.Controller.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Notification client that connects to the NotificationServer and receives FTP
 * change notifications.
 */
public class NotificationClient {
    private static final int SERVER_PORT = 5000;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageListener listener;
    private Thread listenerThread;
    private boolean isConnected;
    private String serverHost;

    /**
     * Interface for handling received messages
     */
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    /**
     * Connect to the notification server
     * 
     * @param serverHost The host address of the notification server
     * @return true if connection successful, false otherwise
     */
    public boolean connect(String serverHost) {
        this.serverHost = serverHost;
        try {
            // Create socket with short timeout (3 seconds) to fail fast if server is not
            // available
            socket = new Socket();
            socket.connect(new java.net.InetSocketAddress(serverHost, SERVER_PORT), 3000); // 3 second timeout

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
            System.out.println("Connected to notification server at " + serverHost + ":" + SERVER_PORT);
            return true;
        } catch (IOException e) {
            System.err.println("Could not connect to notification server at " + serverHost + ":" + SERVER_PORT + " - "
                    + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    /**
     * Set the message listener and start listening for messages
     * 
     * @param listener The listener to handle received messages
     */
    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
        startListening();
    }

    /**
     * Start listening for messages in a background thread
     */
    private void startListening() {
        if (!isConnected || listener == null) {
            return;
        }

        listenerThread = new Thread(new MessageListenerThread(in, listener, this));
        listenerThread.start();
    }

    /**
     * Send a message to the notification server
     * 
     * @param message The message to send
     * @return true if sent successfully, false otherwise
     */
    public boolean sendMessage(String message) {
        if (out != null && isConnected) {
            out.println(message);
            return !out.checkError();
        }
        return false;
    }

    /**
     * Disconnect from the notification server
     */
    public void disconnect() {
        isConnected = false;

        try {
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.interrupt();
            }

            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Disconnected from notification server.");
        } catch (IOException e) {
            System.err.println("Error disconnecting from server: " + e.getMessage());
        }
    }

    /**
     * Check if client is connected
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected && socket != null && !socket.isClosed();
    }
}

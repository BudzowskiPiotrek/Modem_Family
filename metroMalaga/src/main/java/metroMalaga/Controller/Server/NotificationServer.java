package metroMalaga.Controller.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Notification server that manages client connections and broadcasts FTP change
 * notifications.
 * Singleton pattern ensures only one server instance runs.
 */
public class NotificationServer {
    private static NotificationServer instance;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private boolean isRunning;
    private Thread serverThread;

    private NotificationServer() {
        this.clients = new ArrayList<>();
        this.isRunning = false;
    }

    public static synchronized NotificationServer getInstance() {
        if (instance == null) {
            instance = new NotificationServer();
        }
        return instance;
    }

    /**
     * Start the notification server
     */
    public synchronized void start() {
        if (isRunning) {
            System.out.println("Notification server is already running on port 5000.");
            return;
        }

        // Check if server thread exists and is alive (server might have started but
        // failed)
        if (serverThread != null && serverThread.isAlive()) {
            System.out.println("Notification server thread is already active.");
            return;
        }

        try {
            serverThread = new Thread(new ServerListenerThread(this));
            serverThread.setName("NotificationServerThread");
            serverThread.start();
            System.out.println("Notification server thread started.");
        } catch (Exception e) {
            System.err.println("Error starting notification server thread: " + e.getMessage());
            isRunning = false;
        }
    }

    /**
     * Broadcast a message to all connected clients
     * 
     * @param message The message to broadcast
     */
    public synchronized void broadcast(String message) {
        List<ClientHandler> disconnectedClients = new ArrayList<>();

        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (!client.sendMessage(message)) {
                    disconnectedClients.add(client);
                }
            }

            // Remove disconnected clients
            clients.removeAll(disconnectedClients);
        }

        if (!disconnectedClients.isEmpty()) {
            System.out.println("Removed " + disconnectedClients.size() + " disconnected clients");
        }
    }

    /**
     * Stop the notification server
     */
    public synchronized void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        // Close all client connections
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.close();
            }
            clients.clear();
        }

        // Close server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }

        System.out.println("Notification server stopped.");
    }

    // Package-private methods for ServerListenerThread and ClientHandler

    void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    void setRunning(boolean running) {
        this.isRunning = running;
    }

    boolean isRunning() {
        return isRunning;
    }

    void addClient(ClientHandler handler) {
        synchronized (clients) {
            clients.add(handler);
        }
    }
}

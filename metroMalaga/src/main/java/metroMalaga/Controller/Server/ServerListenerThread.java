package metroMalaga.Controller.Server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread responsible for listening to incoming client connections on the
 * notification server.
 */
public class ServerListenerThread implements Runnable {
    private static final int PORT = 5000;
    private NotificationServer server;

    /**
     * Constructor for ServerListenerThread.
     * 
     * @param server The notification server instance.
     */
    public ServerListenerThread(NotificationServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            server.setServerSocket(serverSocket);
            server.setRunning(true);
            System.out.println("Notification server started on port " + PORT);

            while (server.isRunning()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    ClientHandler handler = new ClientHandler(clientSocket, server);
                    server.addClient(handler);
                    new Thread(handler).start();
                } catch (IOException e) {
                    if (server.isRunning()) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (BindException e) {
            System.err.println("IMPORTANT: Could not start notification server - Port " + PORT + " is already in use.");
            System.err.println(
                    "This usually means another instance is already running or the port wasn't released properly.");
            System.err.println("Solution: Close other instances or wait a moment for the port to be released.");
            server.setRunning(false);
        } catch (IOException e) {
            System.err.println("Could not start notification server: " + e.getMessage());
            server.setRunning(false);
        }
    }
}

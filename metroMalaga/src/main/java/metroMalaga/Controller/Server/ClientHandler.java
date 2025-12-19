package metroMalaga.Controller.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handler for individual client connections to the notification server.
 * Each client has its own handler running in a separate thread.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private NotificationServer server;

    /**
     * Constructor for ClientHandler.
     * 
     * @param socket The client socket.
     * @param server The main server instance.
     */
    public ClientHandler(Socket socket, NotificationServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error creating client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // Listen for messages from this client and rebroadcast to all clients
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message from client: " + message);
                // Rebroadcast the message to all connected clients
                server.broadcast(message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Sends a message to the connected client.
     * 
     * @param message The message to send.
     * @return true if sent successfully, false otherwise.
     */
    public boolean sendMessage(String message) {
        if (out != null && !socket.isClosed()) {
            try {
                out.println(message);
                return !out.checkError();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Closes the client connection and resources.
     */
    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }
}

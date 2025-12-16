package metroMalaga.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handler for individual client connections to the notification server.
 * Each client has its own handler running in a separate thread.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private NotificationServer server;

    public ClientHandler(Socket socket, NotificationServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error creating client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // Keep connection alive, just listening for disconnection
        try {
            while (!socket.isClosed() && socket.isConnected()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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

    public void close() {
        try {
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

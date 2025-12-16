package metroMalaga.Controller;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Thread responsible for listening to messages from the notification server.
 */
public class MessageListenerThread implements Runnable {
    private BufferedReader in;
    private NotificationClient.MessageListener listener;
    private NotificationClient client;

    public MessageListenerThread(BufferedReader in, NotificationClient.MessageListener listener,
            NotificationClient client) {
        this.in = in;
        this.listener = listener;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String message;
            while (client.isConnected() && (message = in.readLine()) != null) {
                final String receivedMessage = message;
                System.out.println("Received notification: " + receivedMessage);
                listener.onMessageReceived(receivedMessage);
            }
        } catch (IOException e) {
            if (client.isConnected()) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }
}

package metroMalaga.Socket;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServidorSocket {
	private final int port = 12345;
	private final List<PrintWriter> clientWriters = new ArrayList<>();

	public ServidorSocket() {
		// Hilo principal que acepta conexiones de clientes
		new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Servidor Socket escuchando en puerto " + port);
				while (true) {
					new ManejadorCliente(serverSocket.accept(), this).start();
				}
			} catch (IOException e) {
				System.err.println("Error en ServidorSocket: " + e.getMessage());
			}
		}).start();
	}

	// Método para registrar y desregistrar clientes
	public synchronized void addWriter(PrintWriter writer) {
		clientWriters.add(writer);
	}

	public synchronized void removeWriter(PrintWriter writer) {
		clientWriters.remove(writer);
	}

	// Método CLAVE: Notifica a todos los clientes que hay un cambio
	public synchronized void notificarCambio() {
		for (PrintWriter writer : clientWriters) {
			writer.println("RELOAD"); // Mensaje de evento
			writer.flush();
		}
	}
}

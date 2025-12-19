package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;

/**
 * Task for toggling the read/unread status of an email on the server.
 */
public class ToggleReadStatusTask implements Runnable {

	private final HandleSMTP backend;
	private final String uid;
	private final boolean newStatus;

	/**
	 * Constructor.
	 * 
	 * @param backend   The backend service.
	 * @param uid       The unique ID of the email.
	 * @param newStatus The new read status (true=read, false=unread).
	 */
	public ToggleReadStatusTask(HandleSMTP backend, String uid, boolean newStatus) {
		this.backend = backend;
		this.uid = uid;
		this.newStatus = newStatus;
	}

	@Override
	public void run() {
		try {
			backend.updateReadStatusIMAP(uid, newStatus);
		} catch (Exception e) {
		}
	}
}
package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.HandleSMTP;

public class ToggleReadStatusTask implements Runnable {

	private final HandleSMTP backend;
	private final String uid;
	private final boolean newStatus;

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
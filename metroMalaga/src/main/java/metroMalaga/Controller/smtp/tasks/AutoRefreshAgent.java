package metroMalaga.Controller.smtp.tasks;

import metroMalaga.Controller.smtp.ButtonHandleSMTP;

/**
 * Runnable agent that periodically triggers an inbox refresh.
 * Runs in a separate thread.
 */
public class AutoRefreshAgent implements Runnable {

	private final ButtonHandleSMTP controller;

	/**
	 * Constructor.
	 * 
	 * @param controller The main SMTP controller.
	 */
	public AutoRefreshAgent(ButtonHandleSMTP controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				controller.refreshInbox(true);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
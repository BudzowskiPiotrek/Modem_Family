package metroMalaga;

import metroMalaga.View.PanelLogin;

/**
 * Entry point of the Metro Malaga application.
 * This class is responsible for bootstrapping the application by 
 * initializing and displaying the primary login interface.
 * * @author Modem Family
 * @version 1.0
 */
public class Main {
	
	/**
	 * Application main method.
	 * Launches the graphical user interface by creating an instance 
	 * of the Login Panel.
	 * * @param args Command line arguments (not used).
	 */
	public static void main(String[] args) {
		PanelLogin panelLogin = new PanelLogin();
		panelLogin.setVisible(true);
	}
}
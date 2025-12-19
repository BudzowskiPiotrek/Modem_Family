package metroMalaga;

import metroMalaga.View.PanelLogin;

/**
 * Entry point for the Metro Malaga application.
 * This class contains the main method that initializes and launches the user interface.
 * * @author Modem Family / Developer
 * @version 1.0
 */
public class Main {
    
	/**
	 * Starts the application by instantiating and displaying the login panel.
	 * * @param args command line arguments (not used in this application).
	 */
	public static void main(String[] args) {
		PanelLogin panelLogin = new PanelLogin();
		panelLogin.setVisible(true);
	}
}
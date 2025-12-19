package metroMalaga.Controller.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.ServiceLogin;
import metroMalaga.Model.Usuario;
import metroMalaga.View.PanelLogin;
import metroMalaga.View.PanelMenu;

/**
 * Handles the login attempt logic for the application.
 */
public class LoginAttempt implements ActionListener {
	private final JTextField userField;
	private final JPasswordField passwordField;
	private final JButton loginButton;
	private Usuario user;
	private final PanelLogin panelLogin;
	private ServiceLogin sl = new ServiceLogin();
	private Common cn = new Common();

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8,}$";

	/**
	 * Constructor for LoginAttempt.
	 * 
	 * @param userField     The username text field.
	 * @param passwordField The password text field.
	 * @param loginButton   The login button.
	 * @param panelLogin    The login panel instance.
	 */
	public LoginAttempt(JTextField userField, JPasswordField passwordField, JButton loginButton,
			PanelLogin panelLogin) {
		super();
		this.userField = userField;
		this.passwordField = passwordField;
		this.loginButton = loginButton;
		this.panelLogin = panelLogin;
		this.loginButton.addActionListener(this);

		// Agregar KeyListener para que Enter ejecute el login
		LoginEnterKeyListener enterKeyListener = new LoginEnterKeyListener(loginButton, this);

		this.userField.addKeyListener(enterKeyListener);
		this.passwordField.addKeyListener(enterKeyListener);
	}

	/**
	 * Gets the authenticated user.
	 * 
	 * @return The authenticated user.
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * Handles the action event triggered by the login button or enter key.
	 * Validates input and attempts authentication.
	 * 
	 * @param e The action event.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String username = userField.getText();
		String password = new String(passwordField.getPassword());
		if (username.trim().isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The username and password cannot be empty.", "Validation Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!username.matches(USERNAME_PATTERN)) {
			JOptionPane.showMessageDialog(null, "The user can only contain letters (a-z, A-Z) and numbers (0-9).",
					"Format Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!password.matches(PASSWORD_PATTERN)) {
			JOptionPane.showMessageDialog(null,
					"The password must be at least 8 characters long and contain only letters and numbers.",
					"Format Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		boolean isAuthenticated = sl.authenticateUser(username, password);

		if (isAuthenticated) {
			this.user = sl.getUserData(username, password);

			if (this.user != null) {
				JOptionPane.showMessageDialog(null, "Welcome, " + username + "Access granted.", "Login successful",
						JOptionPane.INFORMATION_MESSAGE);

				cn.registerLog(username, "Successful login attempt");
				PanelMenu panelMenu = new PanelMenu(user);
				panelMenu.setVisible(true);
				this.panelLogin.loginSuccessful();

			} else {

				JOptionPane.showMessageDialog(null, "Error loading user data. Please try again.", "Internal Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Incorrect username or password. Please try again.",
					"Authentication Error", JOptionPane.WARNING_MESSAGE);

			cn.registerLog(username, "Login attempt failed");
			return;
		}
	}
}

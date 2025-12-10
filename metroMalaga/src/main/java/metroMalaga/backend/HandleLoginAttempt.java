package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import metroMalaga.Clases.Usuario;

public class HandleLoginAttempt implements ActionListener {
	private final JTextField userField;
	private final JPasswordField passwordField;
	private final JButton loginButton;
	private Usuario user;
	private ServiceLogin sl = new ServiceLogin();

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8,}$";

	public HandleLoginAttempt(JTextField userField, JPasswordField passwordField, JButton loginButton) {
		super();
		this.userField = userField;
		this.passwordField = passwordField;
		this.loginButton = loginButton;
	}

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

		if (sl.authenticateUser(username, password)) {
			this.user = sl.getUserData(username);
		} else {
			JOptionPane.showMessageDialog(null, "", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
}

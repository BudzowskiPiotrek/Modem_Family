package metroMalaga.frontend;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import metroMalaga.backend.HandleLoginAttempt;
import metroMalaga.backend.PlaceholderPasswordFieldHandler;
import metroMalaga.backend.PlaceholderTextFieldHandler;

public class PanelLogin extends JFrame {

	private JTextField userField;
	private JPasswordField passwordField;
	private JButton buttonLogin;
	private JLabel labelLogin;

	public PanelLogin() {
		settingsFrame();
		settingsComponents();
		addComponentsFrame();
		
		HandleLoginAttempt handler = new HandleLoginAttempt(userField,passwordField ,buttonLogin);
		
	}

	private void settingsComponents() {
		settingsLabel();
		settingsUser();
		settingsPass();
		settingsButton();
	}

	private void settingsLabel() {
		labelLogin = new JLabel("Login", SwingConstants.CENTER);
		labelLogin.setFont(new Font("Arial", Font.BOLD, 36));
	}

	private void addComponentsFrame() {
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        add(labelLogin, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        add(userField, gbc);
        
        gbc.gridy = 2;
        add(passwordField, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 20, 10, 20);
        add(buttonLogin, gbc);
	}

	private void settingsButton() {
		buttonLogin = new JButton();
		buttonLogin.setBounds(150, 150, 100, 40);
		buttonLogin.setBackground(Color.RED);
		buttonLogin.setForeground(Color.BLACK);
		buttonLogin.setText("LOGIN");
		buttonLogin.setFocusPainted(false);
		buttonLogin.setBorder(new EmptyBorder(12, 20, 12, 20));
        buttonLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	private void settingsPass() {
		passwordField = new JPasswordField(20);
		passwordField.setBounds(100, 50, 200, 30);
		passwordField.setForeground(Color.GRAY);
		passwordField.setText("Password");
		passwordField.addFocusListener(new PlaceholderPasswordFieldHandler(passwordField, "Password"));
	}

	private void settingsUser() {
		userField = new JTextField(20);
		userField.setBounds(100, 50, 200, 30);
		userField.setForeground(Color.GRAY);
		userField.setText("Username");
		userField.addFocusListener(new PlaceholderTextFieldHandler(userField, "Username"));
	}

	private void settingsFrame() {
		setTitle("Login - Metro Málaga");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(new GridBagLayout());
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		PanelLogin panelLogin = new PanelLogin();
		panelLogin.setVisible(true);
	}

}

package metroMalaga.View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import metroMalaga.Controller.login.LoginAttempt;
import metroMalaga.Controller.login.LoginButton;
import metroMalaga.Controller.login.LoginPlaceholderPasswordFieldHandler;
import metroMalaga.Controller.login.LoginPlaceholderTextFieldHandler;
import metroMalaga.Model.Usuario;

public class PanelLogin extends JFrame {

	private JTextField userField;
	private JPasswordField passwordField;
	private JButton buttonLogin;
	private JLabel labelLogin;

	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);
	private final Color P5_GRAY_PLACEHOLDER = new Color(150, 150, 150);

	private final Font P5_INPUT_FONT = new Font("SansSerif", Font.BOLD | Font.ITALIC, 24);
	private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 24);

	public PanelLogin() {
		settingsFrame();
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBackground(P5_RED);
		setContentPane(backgroundPanel);

		settingsComponents();
		addComponentsFrame(backgroundPanel);
		LoginAttempt handler = new LoginAttempt(userField, passwordField, buttonLogin, this);

		this.getContentPane().setFocusable(true);
		this.getContentPane().requestFocusInWindow();
	}

	public void loginSuccessful() {
		this.setVisible(false);
		this.dispose();
	}

	private void settingsComponents() {
		settingsLabel();
		settingsUser();
		settingsPass();
		settingsButton();
	}

	private void settingsLabel() {
		String p5StyledText = "<html>"
				+ "<span style='background-color:black; color:white; font-size:32px; font-family:Sans-serif; font-style:italic;'>L</span>"
				+ "<span style='background-color:red; color:black; font-size:40px; font-weight:bold; font-family:Serif;'>O</span>"
				+ "<span style='color:black; font-size:35px; font-family:Sans-serif; font-weight:bold;'>G</span>"
				+ "<span style='background-color:black; color:red; font-size:38px; font-style:italic;'>I</span>"
				+ "<span style='background-color:white; color:black; font-size:30px; font-weight:bold;'>N</span>"
				+ "</html>";

		labelLogin = new JLabel(p5StyledText, SwingConstants.CENTER);
		labelLogin.setBorder(new MatteBorder(0, 0, 5, 0, P5_BLACK));
	}

	private void settingsUser() {
		userField = new JTextField(20);

		styleP5Field(userField, "Username");

		MatteBorder focusedBorder = new MatteBorder(3, 3, 5, 5, P5_BRIGHT_RED);
		MatteBorder unfocusedBorder = new MatteBorder(3, 3, 5, 3, P5_BLACK);
		EmptyBorder padding = new EmptyBorder(5, 15, 5, 15);

		userField.addFocusListener(new LoginPlaceholderTextFieldHandler(userField, "Username", P5_WHITE, P5_WHITE,
				P5_GRAY_PLACEHOLDER, focusedBorder, unfocusedBorder, padding));
	}

	private void settingsPass() {
		passwordField = new JPasswordField(15);

		styleP5Field(passwordField, "Password");
		passwordField.setEchoChar((char) 0);

		MatteBorder focusedBorder = new MatteBorder(3, 3, 5, 5, P5_BRIGHT_RED);
		MatteBorder unfocusedBorder = new MatteBorder(3, 3, 5, 3, P5_BLACK);
		EmptyBorder padding = new EmptyBorder(5, 15, 5, 15);

		passwordField.addFocusListener(new LoginPlaceholderPasswordFieldHandler(passwordField, "Password", P5_WHITE,
				P5_WHITE, P5_GRAY_PLACEHOLDER, focusedBorder, unfocusedBorder, padding));
	}

	private void styleP5Field(JTextField field, String placeholder) {
		field.setPreferredSize(new Dimension(250, 50));
		field.setBackground(P5_BLACK);
		field.setForeground(P5_GRAY_PLACEHOLDER);
		field.setCaretColor(P5_BRIGHT_RED);
		field.setText(placeholder);

		field.setFont(P5_INPUT_FONT);

		field.setBorder(new MatteBorder(3, 3, 5, 3, P5_BLACK));
		field.setBorder(new CompoundBorder(field.getBorder(), new EmptyBorder(5, 15, 5, 15)));
	}

	private void settingsButton() {
		buttonLogin = new JButton("LOGIN");
		buttonLogin.setPreferredSize(new Dimension(180, 60));
		buttonLogin.setBackground(P5_BRIGHT_RED);
		buttonLogin.setForeground(P5_WHITE);
		buttonLogin.setFont(P5_BUTTON_FONT);
		buttonLogin.setFocusPainted(false);
		buttonLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

		MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BLACK);
		MatteBorder hoverBorder = new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED);

		buttonLogin.setBorder(defaultBorder);

		buttonLogin.addMouseListener(new LoginButton(buttonLogin, P5_BRIGHT_RED, P5_WHITE, P5_BLACK, P5_BRIGHT_RED,
				defaultBorder, hoverBorder));
	}

	private void addComponentsFrame(Container container) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridy = 0;
		gbc.insets = new Insets(30, 20, 40, 20);
		container.add(labelLogin, gbc);

		gbc.gridy = 1;
		gbc.insets = new Insets(10, 40, 15, 40);
		container.add(userField, gbc);

		gbc.gridy = 2;
		gbc.insets = new Insets(10, 40, 15, 40);
		container.add(passwordField, gbc);

		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 20, 30, 20);
		container.add(buttonLogin, gbc);
	}

	private void settingsFrame() {
		setTitle("Login - Metro Malaga");
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
}
package metroMalaga.frontend.menu;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import metroMalaga.Clases.Usuario;
import metroMalaga.backend.HandleMenu;
import metroMalaga.frontend.crud.PanelCrud;
import metroMalaga.frontend.ftp.PanelFTP;
import metroMalaga.frontend.smtp.PanelSMTP;

public class PanelMenu extends JFrame {
	public ArrayList<JButton> buttons;
	public ArrayList<String> buttonsName;
	private Usuario user;

	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 24);

	public PanelMenu(Usuario user) {
		this.user = user;
		this.buttonsName = new ArrayList<>();
		this.buttons = new ArrayList<>();

		createButtonsName();
		createButtons();
		addListenerButtons();
		propertiesWindow();
		addButtons();
		setTitle();

		HandleMenu handler = new HandleMenu(this, buttons, user);
		this.setVisible(true);
	}

	public void pressedButton(Usuario user, String nameButton) {

		if (nameButton.equals("CRUD")) {
			PanelCrud panelCrud = new PanelCrud(user);
			panelCrud.setVisible(true);
		} else if (nameButton.equals("FTP")) {
			PanelFTP panelFtp = new PanelFTP(user);
			panelFtp.setVisible(true);
		} else if (nameButton.equals("SMTP")) {
			PanelSMTP panelSmtp = new PanelSMTP(user);
			panelSmtp.setVisible(true);
		}
		this.setVisible(false);
		this.dispose();
	}

	private void addListenerButtons() {
		for (JButton bns : buttons) {
			bns.addMouseListener(new MouseListener(bns));
		}
	}

	private void propertiesWindow() {
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(P5_RED);

		this.setSize(700, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	private void setTitle() {
		this.setTitle("Menú Principal - centimetro Málaga");
	}

	private void createButtonsName() {
		buttonsName.add("CRUD");
		buttonsName.add("FTP");
		buttonsName.add("SMTP");
		buttonsName.add("Salir");
	}

	private void createButtons() {
		for (String bn : buttonsName) {
			JButton button = new JButton(bn);

			button.setPreferredSize(new Dimension(250, 60));
			button.setBackground(P5_BLACK);
			button.setForeground(P5_WHITE);
			button.setFont(P5_BUTTON_FONT);
			button.setFocusPainted(false);
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));

			MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED);
			button.setBorder(defaultBorder);

			buttons.add(button);
		}
	}

	private void addButtons() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(15, 0, 15, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (JButton button : buttons) {
			this.add(button, gbc);
			gbc.gridy++;
		}
	}
}
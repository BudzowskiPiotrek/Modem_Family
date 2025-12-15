package metroMalaga.frontend.menu;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import metroMalaga.Clases.Usuario;
import metroMalaga.backend.MenuSelect;
import metroMalaga.frontend.crud.PanelCrud;
import metroMalaga.frontend.ftp.PanelFTP;
import metroMalaga.frontend.smtp.PanelSMTP;

// Importamos la clase Usuario ya que el Login nos la pasa

public class PanelMenu extends JFrame {

	// Mantenemos tus listas
	public ArrayList<JButton> buttons;
	public ArrayList<String> buttonsName;
	private Usuario user;

	// --- ESTÉTICA PERSONA 5 (Copiada del Login) ---
	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	// Usamos la fuente de botones definida en el Login
	private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 24);

	public PanelMenu(Usuario user) {
		this.user = user;
		this.buttonsName = new ArrayList<>();
		this.buttons = new ArrayList<>();

		// Mantenemos tu orden de llamadas original

		createButtonsName();
		createButtons();
		addListenerButtons();
		// Configuramos la ventana ANTES de añadir botones para tener el Layout listo
		propertiesWindow();
		addButtons();
		setTitle();

		MenuSelect handler = new MenuSelect(this, buttons, user);
		this.setVisible(true);
	}

	public void disposeWindow() {
		this.setVisible(false);
		this.dispose();
	}

	private void addListenerButtons() {
		for (JButton bns : buttons) {
			bns.addMouseListener(new MouseListener(bns));
		}
	}

	private void propertiesWindow() {
		// Usamos GridBagLayout para centrar los elementos como en el Login
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(P5_RED); // Fondo Rojo P5

		this.setSize(700, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	private void setTitle() {
		// Título de la ventana
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

			// --- APLICANDO ESTÉTICA AL BOTÓN ---
			button.setPreferredSize(new Dimension(250, 60)); // Un poco más anchos
			button.setBackground(P5_BLACK); // Fondo negro por defecto (para contrastar con el fondo rojo de la ventana)
			button.setForeground(P5_WHITE); // Texto blanco
			button.setFont(P5_BUTTON_FONT);
			button.setFocusPainted(false);
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));

			// Borde estilo cómic/Persona 5
			MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED);
			button.setBorder(defaultBorder);

			// Efecto Hover (Ratón encima) - Invertimos colores
			buttons.add(button);
		}
	}

	private void addButtons() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		// Margen entre botones (Arriba, Izquierda, Abajo, Derecha)
		gbc.insets = new Insets(15, 0, 15, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (JButton button : buttons) {
			this.add(button, gbc);
			gbc.gridy++; // Siguiente botón en la siguiente fila
		}
	}
}
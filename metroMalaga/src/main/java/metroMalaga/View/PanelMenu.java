package metroMalaga.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Usuario;

public class PanelMenu extends JFrame {

	private Usuario user;
	private JTabbedPane tabbedPane;
	private MenuSelect menuController;

	// --- ESTÉTICA PERSONA 5 ---
	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	public PanelMenu(Usuario user) {
		this.user = user;

		propertiesWindow();
		createTabbedPane();
		setTitle();

		this.setVisible(true);
	}

	public void disposeWindow() {
		this.setVisible(false);
		this.dispose();
	}

	private void propertiesWindow() {
		this.setLayout(new BorderLayout());
		this.getContentPane().setBackground(P5_RED);

		this.setSize(1100, 750);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	private void setTitle() {
		this.setTitle("Menú Principal - Metro Málaga");
	}

	private void createTabbedPane() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		// Aplicar estilo Persona 5 al JTabbedPane
		tabbedPane.setBackground(P5_BLACK);
		tabbedPane.setForeground(P5_WHITE);
		tabbedPane.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));

		// Crear el controlador que manejará las pestañas
		menuController = new MenuSelect(this, tabbedPane, user);

		// Añadir las pestañas (inicialmente vacías, se cargarán bajo demanda)
		tabbedPane.addTab("CRUD", null);
		tabbedPane.addTab("FTP", null);
		tabbedPane.addTab("SMTP", null);
		tabbedPane.addTab("Salir", createExitPanel());

		// Aplicar estilo personalizado a las pestañas
		styleTabPane();

		this.add(tabbedPane, BorderLayout.CENTER);
	}

	private void styleTabPane() {
		// Personalizar el UI del JTabbedPane
		UIManager.put("TabbedPane.selected", P5_BRIGHT_RED);
		UIManager.put("TabbedPane.background", P5_BLACK);
		UIManager.put("TabbedPane.foreground", P5_WHITE);
		UIManager.put("TabbedPane.focus", P5_BRIGHT_RED);
		UIManager.put("TabbedPane.contentAreaColor", P5_RED);
		UIManager.put("TabbedPane.borderHightlightColor", P5_BRIGHT_RED);

		SwingUtilities.updateComponentTreeUI(tabbedPane);
	}

	private JPanel createExitPanel() {
		JPanel exitPanel = new JPanel(new GridBagLayout());
		exitPanel.setBackground(P5_RED);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(P5_BLACK);
		contentPanel.setBorder(new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED));

		JLabel lblMessage = new JLabel("¿Desea salir de la aplicación?");
		lblMessage.setFont(new Font("Dialog", Font.BOLD, 24));
		lblMessage.setForeground(P5_WHITE);
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		buttonPanel.setBackground(P5_BLACK);

		JButton btnConfirm = new JButton("Sí, Salir");
		styleExitButton(btnConfirm, P5_BRIGHT_RED, P5_WHITE);
		btnConfirm.addActionListener(e -> System.exit(0));

		JButton btnCancel = new JButton("No, Volver");
		styleExitButton(btnCancel, P5_WHITE, P5_BLACK);
		btnCancel.addActionListener(e -> tabbedPane.setSelectedIndex(0));

		buttonPanel.add(btnCancel);
		buttonPanel.add(btnConfirm);

		contentPanel.add(lblMessage);
		contentPanel.add(buttonPanel);

		exitPanel.add(contentPanel);

		return exitPanel;
	}

	private void styleExitButton(JButton btn, Color bg, Color fg) {
		btn.setPreferredSize(new Dimension(180, 50));
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED));

		// Hover effect
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			Color originalBg = btn.getBackground();
			Color originalFg = btn.getForeground();

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				btn.setBackground(originalFg);
				btn.setForeground(originalBg);
				btn.setBorder(new MatteBorder(4, 4, 8, 6, P5_BLACK));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				btn.setBackground(originalBg);
				btn.setForeground(originalFg);
				btn.setBorder(new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED));
			}
		});
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public Usuario getUser() {
		return user;
	}
}
package metroMalaga.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import metroMalaga.Controller.Common;
import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

/**
 * Main menu panel housing the application tabs.
 */
public class PanelMenu extends JFrame {

	private Usuario user;
	private JTabbedPane tabbedPane;
	private MenuSelect menuController;

	private JButton btnLanguage;
	private JButton btnDarkMode;
	private JPanel topPanel;

	private JLabel lblMessage;
	private JButton btnConfirm, btnCancel;

	private final Color P5_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	/**
	 * Constructor for PanelMenu.
	 * 
	 * @param user The logged-in user.
	 */
	public PanelMenu(Usuario user) {
		this.user = user;

		propertiesWindow();
		createTabbedPane();
		createTopBar();
		setTitle();

		applyTheme();

		this.setVisible(true);
	}

	/**
	 * Disposes the current window.
	 */
	public void disposeWindow() {
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Sets the window properties.
	 */
	private void propertiesWindow() {
		this.setLayout(new BorderLayout());
		this.setSize(1100, 750);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	/**
	 * Sets the window title.
	 */
	private void setTitle() {
		this.setTitle(Language.get(91));
	}

	/**
	 * Creates the top bar with language and theme toggles.
	 */
	private void createTopBar() {
		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

		String langText = Language.getCurrentLanguage().equals("espanol") ? "ES" : "EN";
		btnLanguage = new JButton(langText);
		styleTopButton(btnLanguage);
		btnLanguage.addActionListener(e -> toggleLanguage());

		btnDarkMode = new JButton(Common.isDarkMode ? "☀️" : "🌙");
		styleTopButton(btnDarkMode);
		btnDarkMode.addActionListener(e -> toggleTheme());

		topPanel.add(btnLanguage);
		topPanel.add(btnDarkMode);

		this.add(topPanel, BorderLayout.NORTH);
	}

	/**
	 * Styles the buttons in the top bar.
	 * 
	 * @param btn The button to style.
	 */
	private void styleTopButton(JButton btn) {
		btn.setFont(new Font("Dialog", Font.BOLD, 14));
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(80, 35));
	}

	/**
	 * Toggles the application theme between light and dark modes.
	 */
	private void toggleTheme() {
		Common.isDarkMode = !Common.isDarkMode;

		btnDarkMode.setText(Common.isDarkMode ? "☀️" : "🌙");

		applyTheme();

		if (menuController != null) {
			menuController.updateActivePanelTheme();
		}
	}

	/**
	 * Applies the current theme to the menu components.
	 */
	public void applyTheme() {
		boolean p5Mode = Common.isDarkMode;

		Color bgColor = p5Mode ? P5_BLACK : new Color(230, 230, 230);
		Color headerBg = p5Mode ? P5_BLACK : new Color(230, 230, 230);
		Color btnBg = p5Mode ? P5_RED : Common.getPanelBackground();
		Color btnFg = p5Mode ? P5_WHITE : Common.getText();
		Color btnBorder = p5Mode ? P5_BLACK : Common.getBorder();

		this.getContentPane().setBackground(bgColor);
		if (topPanel != null)
			topPanel.setBackground(headerBg);

		updateButtonStyle(btnLanguage, btnBg, btnFg, btnBorder);
		updateButtonStyle(btnDarkMode, btnBg, btnFg, btnBorder);

		if (tabbedPane != null) {
			Color tabBg = p5Mode ? P5_BLACK : new Color(230, 230, 230);
			Color tabFg = p5Mode ? P5_WHITE : Color.BLACK;
			Color tabSel = p5Mode ? P5_RED : Common.getDanger();

			tabbedPane.setBackground(tabBg);
			tabbedPane.setForeground(tabFg);

			UIManager.put("TabbedPane.selected", tabSel);
			UIManager.put("TabbedPane.contentAreaColor", bgColor);
			UIManager.put("TabbedPane.background", tabBg);
			UIManager.put("TabbedPane.darkShadow", tabBg);
			UIManager.put("TabbedPane.light", tabBg);
			UIManager.put("TabbedPane.highlight", tabBg);
			UIManager.put("TabbedPane.shadow", tabBg);

			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				Component comp = tabbedPane.getComponentAt(i);
				if (comp != null && comp instanceof JPanel) {
					comp.setBackground(bgColor);
				}
			}

			SwingUtilities.updateComponentTreeUI(tabbedPane);
		}

		this.repaint();
	}

	/**
	 * Updates the style of a button based on the theme.
	 * 
	 * @param btn    The button.
	 * @param bg     Background color.
	 * @param fg     Foreground color.
	 * @param border Border color.
	 */
	private void updateButtonStyle(JButton btn, Color bg, Color fg, Color border) {
		if (btn == null)
			return;
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setBorder(new MatteBorder(2, 2, 4, 4, border));
	}

	/**
	 * Toggles the application language between Spanish and English.
	 */
	private void toggleLanguage() {
		if (Language.getCurrentLanguage().equals("espanol")) {
			Language.setEnglish();
			btnLanguage.setText("EN");
		} else {
			Language.setSpanish();
			btnLanguage.setText("ES");
		}
		updateAllTexts();
		notifyPanelsLanguageChange();
	}

	/**
	 * Notifies all panels about a language change.
	 */
	private void notifyPanelsLanguageChange() {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp instanceof PanelSMTP) {
				((PanelSMTP) comp).updateAllTexts();
			} else if (comp instanceof CrudFrontend) {
				((CrudFrontend) comp).updateAllTexts();
			} else if (comp instanceof PanelFTP) {
				((PanelFTP) comp).updateAllTexts();
			}
		}
	}

	/**
	 * Updates all text labels based on the current language.
	 */
	public void updateAllTexts() {
		setTitle();

		tabbedPane.setTitleAt(0, Language.get(92));
		tabbedPane.setTitleAt(1, Language.get(93));
		tabbedPane.setTitleAt(2, Language.get(94));
		tabbedPane.setTitleAt(3, Language.get(95));

		if (lblMessage != null)
			lblMessage.setText(Language.get(96));
		if (btnConfirm != null)
			btnConfirm.setText(Language.get(97));
		if (btnCancel != null)
			btnCancel.setText(Language.get(98));

		revalidate();
		repaint();
	}

	/**
	 * Creates the tabbed pane and initializes tabs.
	 */
	private void createTabbedPane() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));

		tabbedPane.addTab(Language.get(92), null);
		tabbedPane.addTab(Language.get(93), null);
		tabbedPane.addTab(Language.get(94), null);
		tabbedPane.addTab(Language.get(95), createExitPanel());

		menuController = new MenuSelect(this, tabbedPane, user);

		this.add(tabbedPane, BorderLayout.CENTER);
	}

	/**
	 * Creates the exit confirmation panel.
	 * 
	 * @return The exit panel.
	 */
	private JPanel createExitPanel() {
		JPanel exitPanel = new JPanel(new GridBagLayout());
		exitPanel.setOpaque(false);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(P5_BLACK);
		contentPanel.setBorder(new MatteBorder(4, 4, 8, 6, P5_RED));

		lblMessage = new JLabel(Language.get(96));
		lblMessage.setFont(new Font("Dialog", Font.BOLD, 24));
		lblMessage.setForeground(P5_WHITE);
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		buttonPanel.setBackground(P5_BLACK);

		btnConfirm = new JButton(Language.get(97));
		styleExitButton(btnConfirm, P5_RED, P5_WHITE);

		btnCancel = new JButton(Language.get(98));
		styleExitButton(btnCancel, P5_WHITE, P5_BLACK);

		buttonPanel.add(btnCancel);
		buttonPanel.add(btnConfirm);

		contentPanel.add(lblMessage);
		contentPanel.add(buttonPanel);

		exitPanel.add(contentPanel);

		return exitPanel;
	}

	/**
	 * Styles the exit buttons.
	 * 
	 * @param btn The button.
	 * @param bg  Background color.
	 * @param fg  Foreground color.
	 */
	private void styleExitButton(JButton btn, Color bg, Color fg) {
		btn.setPreferredSize(new Dimension(180, 50));
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(new MatteBorder(4, 4, 8, 6, P5_RED));

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
				btn.setBorder(new MatteBorder(4, 4, 8, 6, P5_RED));
			}
		});
	}

	// Getters

	/**
	 * Gets the tabbed pane.
	 * 
	 * @return The tabbed pane.
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Gets the current user.
	 * 
	 * @return The user.
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * Gets the confirm exit button.
	 * 
	 * @return The confirm button.
	 */
	public JButton getBtnConfirm() {
		return btnConfirm;
	}

	/**
	 * Gets the cancel exit button.
	 * 
	 * @return The cancel button.
	 */
	public JButton getBtnCancel() {
		return btnCancel;
	}
}

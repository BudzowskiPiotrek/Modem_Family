package metroMalaga.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

public class PanelMenu extends JFrame {

	private Usuario user;
	private JTabbedPane tabbedPane;
	private MenuSelect menuController;
	private JButton btnLanguage;
	private JLabel lblMessage;
	private JButton btnConfirm, btnCancel;

	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	public PanelMenu(Usuario user) {
		this.user = user;

		propertiesWindow();
		createTabbedPane();
		createLanguageButton();
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
		this.setTitle(Language.get(91));
	}

	private void createLanguageButton() {
		String langText = Language.getCurrentLanguage().equals("espanol") ? "ES" : "EN";
		btnLanguage = new JButton(langText);
		btnLanguage.setFont(new Font("Dialog", Font.BOLD, 14));
		btnLanguage.setBackground(P5_BRIGHT_RED);
		btnLanguage.setForeground(P5_WHITE);
		btnLanguage.setBorder(new MatteBorder(2, 2, 4, 4, P5_BLACK));
		btnLanguage.setFocusPainted(false);
		btnLanguage.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLanguage.setPreferredSize(new Dimension(80, 35));
		btnLanguage.addActionListener(e -> toggleLanguage());

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		topPanel.setBackground(P5_RED);
		topPanel.add(btnLanguage);

		this.add(topPanel, BorderLayout.NORTH);
	}

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

	public void updateAllTexts() {
		setTitle();
		
		tabbedPane.setTitleAt(0, Language.get(92));
		tabbedPane.setTitleAt(1, Language.get(93));
		tabbedPane.setTitleAt(2, Language.get(94));
		tabbedPane.setTitleAt(3, Language.get(95));
		
		lblMessage.setText(Language.get(96));
		btnConfirm.setText(Language.get(97));
		btnCancel.setText(Language.get(98));
		
		revalidate();
		repaint();
	}

	private void createTabbedPane() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.setBackground(P5_BLACK);
		tabbedPane.setForeground(P5_WHITE);
		tabbedPane.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));

		menuController = new MenuSelect(this, tabbedPane, user);

		tabbedPane.addTab(Language.get(92), null);
		tabbedPane.addTab(Language.get(93), null);
		tabbedPane.addTab(Language.get(94), null);
		tabbedPane.addTab(Language.get(95), createExitPanel());

		styleTabPane();

		this.add(tabbedPane, BorderLayout.CENTER);
	}

	private void styleTabPane() {
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

		lblMessage = new JLabel(Language.get(96));
		lblMessage.setFont(new Font("Dialog", Font.BOLD, 24));
		lblMessage.setForeground(P5_WHITE);
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		buttonPanel.setBackground(P5_BLACK);

		btnConfirm = new JButton(Language.get(97));
		styleExitButton(btnConfirm, P5_BRIGHT_RED, P5_WHITE);
		btnConfirm.addActionListener(e -> System.exit(0));

		btnCancel = new JButton(Language.get(98));
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

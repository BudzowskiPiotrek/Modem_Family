package metroMalaga.Controller.ftp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FTPButtonsPanel extends JPanel {

	public final JButton downloadButton = new JButton("📥");
	public final JButton deleteButton = new JButton("❌");
	public final JButton renameButton = new JButton("🖊️");

	public static final Color ACCENT_RED = new Color(220, 53, 69);
	private static final Color BUTTON_GRAY = new Color(255, 193, 7);
	private static final Color DOWNLOAD_BLUE = new Color(0, 123, 255);

	/**
	 * Constructor for FTPButtonsPanel. Initializes the buttons and layout.
	 */
	public FTPButtonsPanel() {
		styleButton(downloadButton, DOWNLOAD_BLUE, Color.WHITE, true);
		styleButton(deleteButton, ACCENT_RED, Color.WHITE, true);
		styleButton(renameButton, BUTTON_GRAY, Color.WHITE, true);
		this.setBackground(Color.WHITE);
		this.setLayout(new GridLayout(1, 3, 5, 0));
		this.add(downloadButton);
		this.add(deleteButton);
		this.add(renameButton);
	}

	/**
	 * Styles a button with the given colors and settings.
	 * 
	 * @param button        The JButton to style.
	 * @param background    The background color.
	 * @param foreground    The foreground color.
	 * @param isTableButton True if the button is inside a table, false otherwise.
	 */
	public static void styleButton(JButton button, Color background, Color foreground, boolean isTableButton) {
		button.setBackground(background);
		button.setForeground(foreground);
		button.setFocusPainted(false);

		if (isTableButton) {
			button.setBorder(new EmptyBorder(5, 5, 5, 5));
			button.setFont(new Font("Dialog", Font.BOLD, 12));
		} else {
			button.setBorder(new EmptyBorder(8, 15, 8, 15));
		}
	}
}

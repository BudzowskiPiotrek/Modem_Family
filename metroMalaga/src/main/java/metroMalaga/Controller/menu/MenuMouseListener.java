package metroMalaga.Controller.menu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

/**
 * Listener for handling mouse events on menu buttons, providing visual
 * feedback.
 */
public class MenuMouseListener implements MouseListener {
	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);
	private MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED);

	private JButton button;

	/**
	 * Constructor for MenuMouseListener.
	 * 
	 * @param button The button to attach the listener to.
	 */
	public MenuMouseListener(JButton button) {
		this.button = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// No action on click, handled by ActionListener
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// No action on press
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// No action on release
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		button.setBackground(P5_WHITE);
		button.setForeground(P5_BLACK);
		button.setBorder(new MatteBorder(4, 4, 8, 6, P5_BLACK));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		button.setBackground(P5_BLACK);
		button.setForeground(P5_WHITE);
		button.setBorder(defaultBorder);
	}

}

package metroMalaga.Controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

public class MenuMouseListener implements java.awt.event.MouseListener {
	private final Color P5_RED = new Color(220, 20, 60);
    private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
    private final Color P5_BLACK = new Color(20, 20, 20);
    private final Color P5_WHITE = new Color(240, 240, 240);
    private MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED);
    
	private JButton button;
	public MenuMouseListener(JButton button) {
		this.button=button;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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

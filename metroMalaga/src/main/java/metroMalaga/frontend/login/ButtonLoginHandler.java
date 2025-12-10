package metroMalaga.frontend.login;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

public class ButtonLoginHandler implements MouseListener {

    private final JButton button;
    private final Color defaultBackground;
    private final Color defaultForeground;
    private final Color hoverBackground;
    private final Color hoverForeground;
    private final MatteBorder defaultBorder;
    private final MatteBorder hoverBorder;

    public ButtonLoginHandler(JButton button, 
                             Color defaultBackground, Color defaultForeground,
                             Color hoverBackground, Color hoverForeground,
                             MatteBorder defaultBorder, MatteBorder hoverBorder) {
        this.button = button;
        this.defaultBackground = defaultBackground;
        this.defaultForeground = defaultForeground;
        this.hoverBackground = hoverBackground;
        this.hoverForeground = hoverForeground;
        this.defaultBorder = defaultBorder;
        this.hoverBorder = hoverBorder;
    }

    public ButtonLoginHandler(JButton button, 
                             Color defaultBackground, Color defaultForeground,
                             Color hoverBackground, Color hoverForeground) {
        this(button, defaultBackground, defaultForeground, 
             hoverBackground, hoverForeground, null, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.setBackground(hoverBackground);
        button.setForeground(hoverForeground);
        
        if (hoverBorder != null) {
            button.setBorder(hoverBorder);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        button.setBackground(defaultBackground);
        button.setForeground(defaultForeground);
        
        if (defaultBorder != null) {
            button.setBorder(defaultBorder);
        }
    }
}

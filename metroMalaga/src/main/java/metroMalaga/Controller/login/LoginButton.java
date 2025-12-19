package metroMalaga.Controller.login;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.border.MatteBorder;

/**
 * Handles mouse interactions for generic buttons on the login screen,
 * providing hover effects.
 */
public class LoginButton implements MouseListener {

    private final JButton button;
    private final Color defaultBackground;
    private final Color defaultForeground;
    private final Color hoverBackground;
    private final Color hoverForeground;
    private final MatteBorder defaultBorder;
    private final MatteBorder hoverBorder;

    /**
     * Constructor with custom borders.
     * 
     * @param button            The button to handle.
     * @param defaultBackground Default background color.
     * @param defaultForeground Default foreground color.
     * @param hoverBackground   Hover background color.
     * @param hoverForeground   Hover foreground color.
     * @param defaultBorder     Default button border.
     * @param hoverBorder       Hover button border.
     */
    public LoginButton(JButton button,
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

    /**
     * Constructor without custom borders.
     * 
     * @param button            The button to handle.
     * @param defaultBackground Default background color.
     * @param defaultForeground Default foreground color.
     * @param hoverBackground   Hover background color.
     * @param hoverForeground   Hover foreground color.
     */
    public LoginButton(JButton button,
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

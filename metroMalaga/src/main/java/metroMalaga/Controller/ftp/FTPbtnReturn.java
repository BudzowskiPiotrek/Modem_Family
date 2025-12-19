package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Usuario;
import metroMalaga.View.PanelFTP;

public class FTPbtnReturn implements ActionListener {

    private PanelFTP panelFTP;
    private Usuario user;
    private MenuSelect menuSelect;

    /**
     * Constructor for FTPbtnReturn class.
     * 
     * @param panelFTP   The FTP panel.
     * @param button     The JButton component.
     * @param user       The current user.
     * @param menuSelect The MenuSelect controller.
     */
    public FTPbtnReturn(PanelFTP panelFTP, JButton button, Usuario user, MenuSelect menuSelect) {
        this.panelFTP = panelFTP;
        this.user = user;
        this.menuSelect = menuSelect;
        button.addActionListener(this);
    }

    /**
     * Handles the action to return to the main menu (tab 0).
     * 
     * @param e The ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Switch back to first tab instead of creating new window
        if (menuSelect != null) {
            menuSelect.switchToTab(0);
        }
    }
}

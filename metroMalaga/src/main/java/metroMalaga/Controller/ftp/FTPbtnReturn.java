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

    public FTPbtnReturn(PanelFTP panelFTP, JButton button, Usuario user, MenuSelect menuSelect) {
        this.panelFTP = panelFTP;
        this.user = user;
        this.menuSelect = menuSelect;
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Switch back to first tab instead of creating new window
        if (menuSelect != null) {
            menuSelect.switchToTab(0);
        }
    }
}

package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import metroMalaga.Clases.Usuario;
import metroMalaga.frontend.ftp.PanelFTP;
import metroMalaga.frontend.menu.PanelMenu;

public class FTPbtnReturn implements ActionListener {
    private final PanelFTP panelFTP;
    private final Usuario user;

    public FTPbtnReturn(PanelFTP panelFTP, JButton returnButton, Usuario user) {
        this.panelFTP = panelFTP;
        this.user = user;
        
        returnButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panelFTP.dispose();
        new PanelMenu(user).setVisible(true); 
    }
}

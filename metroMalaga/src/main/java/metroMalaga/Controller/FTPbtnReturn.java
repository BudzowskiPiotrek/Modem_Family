package metroMalaga.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import metroMalaga.Model.Usuario;
import metroMalaga.View.PanelFTP;
import metroMalaga.View.PanelMenu;

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

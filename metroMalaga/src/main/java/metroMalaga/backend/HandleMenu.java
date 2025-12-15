package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import metroMalaga.Clases.Usuario;
import metroMalaga.frontend.crud.PanelCrud;
import metroMalaga.frontend.ftp.PanelFTP;
import metroMalaga.frontend.menu.PanelMenu;
import metroMalaga.frontend.smtp.PanelSMTP;

public class HandleMenu implements ActionListener {
	private ArrayList<JButton> buttonsMenu;
	private final PanelMenu panelMenu;
	private Usuario user;

	public HandleMenu(PanelMenu panelMenu, ArrayList<JButton> buttonsMenu, Usuario user) {
		this.buttonsMenu = buttonsMenu;
		this.panelMenu = panelMenu;
		this.user = user;

		for (JButton button : buttonsMenu) {
			button.addActionListener(this);
		}
	}

	public Usuario getUser() {
		return user;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String nameButton = button.getText();
		if (nameButton.equals("CRUD")) {
			PanelCrud panelCrud = new PanelCrud(user);
			panelCrud.setVisible(true);
		} else if (nameButton.equals("FTP")) {
			PanelFTP panelFtp = new PanelFTP(user);
			panelFtp.setVisible(true);
		} else if (nameButton.equals("SMTP")) {
			PanelSMTP panelSmtp = new PanelSMTP(user);
			panelSmtp.setVisible(true);
		}
		panelMenu.disposeWindow();

	}

}

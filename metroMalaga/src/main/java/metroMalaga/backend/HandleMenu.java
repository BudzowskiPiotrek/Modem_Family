package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import metroMalaga.Clases.Usuario;
import metroMalaga.frontend.menu.PanelMenu;

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
		String nameButton;
		
		for (JButton button: buttonsMenu) {
			nameButton = button.getText();
			if (nameButton.equalsIgnoreCase("CRUD")) {
				this.panelMenu.pressedButton(user, nameButton);
			} else if (nameButton.equalsIgnoreCase("FTP")) {
				this.panelMenu.pressedButton(user, nameButton);
			}else if(nameButton.equalsIgnoreCase("SMTP")) {
				this.panelMenu.pressedButton(user, nameButton);
			} else if (nameButton.equalsIgnoreCase("Salir")) {
				this.panelMenu.pressedButton(user, nameButton);
			}
			
		}
		
	}

}

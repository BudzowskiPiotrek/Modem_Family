package metroMalaga.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Model.Usuario;
import metroMalaga.View.PanelCrud;
import metroMalaga.View.PanelFTP;
import metroMalaga.View.PanelMenu;
import metroMalaga.View.PanelSMTP;

public class MenuSelect implements ActionListener {
	private ArrayList<JButton> buttonsMenu;
	private final PanelMenu panelMenu;
	private Usuario user;

	public MenuSelect(PanelMenu panelMenu, ArrayList<JButton> buttonsMenu, Usuario user) {
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
		
		switch (nameButton) {
		case "CRUD":
			PanelCrud panelCrud = new PanelCrud(user);
			panelCrud.setVisible(true);
			break;

		case "FTP":
			ServiceFTP service = new ServiceFTP("readwrite"); // + user.getNombre()
			FTPFile[] fileArray = service.listAllFiles();
			List<FTPFile> initialFiles = new ArrayList<>(Arrays.asList(fileArray));

			PanelFTP panelFtp = new PanelFTP(user, service, initialFiles);
			panelFtp.setVisible(true);
			break;

		case "SMTP":
			PanelSMTP panelSmtp = new PanelSMTP(user,panelMenu);
			panelSmtp.setVisible(true);
			break;

		case "Salir":
			break;
		}
		panelMenu.disposeWindow();
	}
}

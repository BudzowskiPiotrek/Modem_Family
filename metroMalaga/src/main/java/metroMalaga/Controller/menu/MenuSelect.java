package metroMalaga.Controller.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.CrudController;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.FTPbtnNewFolder;
import metroMalaga.Controller.ftp.FTPbtnReturn;
import metroMalaga.Controller.ftp.FTPbtnUp;
import metroMalaga.Controller.ftp.FTPbtnUpFile;
import metroMalaga.Controller.ftp.FTPdoubleClick;
import metroMalaga.Controller.ftp.FTPlist;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.View.CrudFrontend;
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
			CrudFrontend crudView = new CrudFrontend(user);
			CrudController crudControl = new CrudController(crudView);
			crudView.setVisible(true);
			break;

		case "FTP":
			startFTP();
			break;

		case "SMTP":
			HandleSMTP handleSmtp = new HandleSMTP();
			PanelSMTP panelSmtp = new PanelSMTP(user, handleSmtp);
			panelSmtp.setVisible(true);
			break;

		case "Salir":
			break;
		}
		panelMenu.disposeWindow();
	}

	private void startFTP() {
		ServiceFTP service = new ServiceFTP(user.getRol().getPermiso());
		FTPFile[] fileArray = service.listAllFiles();
		List<FTPFile> initialFiles = new ArrayList<>(Arrays.asList(fileArray));

		FTPTableModel ftpModel = new FTPTableModel(initialFiles, service);

		PanelFTP panelFtp = new PanelFTP(user, service, initialFiles, ftpModel);
		service.setTableModel(ftpModel);

		new FTPlist(panelFtp.getSearchField(), ftpModel);
		new FTPbtnUpFile(panelFtp.getUploadButton(), service, ftpModel, user);
		new FTPbtnUp(panelFtp.getUpButton(), service, ftpModel);
		new FTPdoubleClick(panelFtp.getFileTable(), service, ftpModel);
		new FTPbtnReturn(panelFtp, panelFtp.getReturnButton(), user);
		new FTPbtnNewFolder(panelFtp.getFolderButton(), service, ftpModel, user);
		panelFtp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		panelFtp.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.out.println("Cerrando recursos FTP...");
				service.disconnectNotifications();
				service.close();
				panelFtp.dispose();
			}
		});

	}
}

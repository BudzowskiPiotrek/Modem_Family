package metroMalaga.Controller.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.FTPRefreshThread;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.View.CrudFrontend;
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
			CrudFrontend crud= new CrudFrontend(user);
			crud.setVisible(true);
			break;

		case "FTP":

			ServiceFTP service = new ServiceFTP("readwrite");
			FTPFile[] fileArray = service.listAllFiles();
			List<FTPFile> initialFiles = new ArrayList<>(Arrays.asList(fileArray));

	        FTPTableModel ftpModel = new FTPTableModel(initialFiles, service); 
	        PanelFTP panelFtp = new PanelFTP(user, service, initialFiles, ftpModel); 
	        
	        FTPRefreshThread refreshThread = new FTPRefreshThread(service, ftpModel); 
	        refreshThread.start();
	        
	        panelFtp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        
	        panelFtp.addWindowListener(new java.awt.event.WindowAdapter() {
	            @Override
	            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	                if (refreshThread != null) {
	                    refreshThread.stopRunning();
	                }
	                panelFtp.dispose(); 
	            }
	        });

			panelFtp.setVisible(true);
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
}

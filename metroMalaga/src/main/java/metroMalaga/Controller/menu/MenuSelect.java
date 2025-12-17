package metroMalaga.Controller.menu;

import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.CrudController;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.FTPbtnNewFolder;
import metroMalaga.Controller.ftp.FTPbtnReturn;
import metroMalaga.Controller.ftp.FTPbtnUp;
import metroMalaga.Controller.ftp.FTPbtnUpFile;
import metroMalaga.Controller.ftp.FTPdoubleClick;
import metroMalaga.Controller.ftp.FTPlist;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.View.CrudFrontend;
import metroMalaga.View.PanelFTP;
import metroMalaga.View.PanelMenu;
import metroMalaga.View.PanelSMTP;

public class MenuSelect implements ChangeListener {
	private final PanelMenu panelMenu;
	private final JTabbedPane tabbedPane;
	private final Usuario user;

	// Track previous tab to cleanup
	private int previousTabIndex = -1;

	// Current active panels (only one at a time)
	private CrudFrontend crudPanel;
	private PanelFTP ftpPanel;
	private PanelSMTP smtpPanel;
	private ServiceFTP ftpService;
	private CrudController crudController;

	public MenuSelect(PanelMenu panelMenu, JTabbedPane tabbedPane, Usuario user) {
		this.panelMenu = panelMenu;
		this.tabbedPane = tabbedPane;
		this.user = user;

		// Add change listener to handle tab switching
		tabbedPane.addChangeListener(this);
	}

	public Usuario getUser() {
		return user;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int selectedIndex = tabbedPane.getSelectedIndex();

		// Cleanup previous tab if it's different
		if (previousTabIndex != -1 && previousTabIndex != selectedIndex && previousTabIndex != 3) {
			cleanupPanel(previousTabIndex);
		}

		// Load new panel
		switch (selectedIndex) {
			case 0: // CRUD
				initializeCRUD();
				break;

			case 1: // FTP
				initializeFTP();
				break;

			case 2: // SMTP
				initializeSMTP();
				break;

			case 3: // Salir - already has content, no cleanup needed
				break;
		}

		// Update previous tab index
		previousTabIndex = selectedIndex;
	}

	private void initializeCRUD() {
		System.out.println("Inicializando panel CRUD...");
		crudPanel = new CrudFrontend(user);
		crudController = new CrudController(crudPanel, this);
		tabbedPane.setComponentAt(0, crudPanel);
	}

	private void initializeFTP() {
		System.out.println("Inicializando panel FTP...");
		ftpService = new ServiceFTP(user.getRol().getPermiso());
		FTPFile[] fileArray = ftpService.listAllFiles();
		List<FTPFile> initialFiles = Arrays.asList(fileArray);

		FTPTableModel ftpModel = new FTPTableModel(initialFiles, ftpService);

		ftpPanel = new PanelFTP(user, ftpService, initialFiles, ftpModel);
		ftpService.setTableModel(ftpModel);

		new FTPlist(ftpPanel.getSearchField(), ftpModel);
		new FTPbtnUpFile(ftpPanel.getUploadButton(), ftpService, ftpModel, user);
		new FTPbtnUp(ftpPanel.getUpButton(), ftpService, ftpModel);
		new FTPdoubleClick(ftpPanel.getFileTable(), ftpService, ftpModel);
		new FTPbtnReturn(ftpPanel, ftpPanel.getReturnButton(), user, this);
		new FTPbtnNewFolder(ftpPanel.getFolderButton(), ftpService, ftpModel, user);

		tabbedPane.setComponentAt(1, ftpPanel);
	}

	private void initializeSMTP() {
		System.out.println("Inicializando panel SMTP...");
		smtpPanel = new PanelSMTP(user);
		smtpPanel.setOnReturnCallback(() -> switchToTab(0));
		tabbedPane.setComponentAt(2, smtpPanel);
	}

	/**
	 * Cleanup panel resources when switching away
	 */
	private void cleanupPanel(int tabIndex) {
		switch (tabIndex) {
			case 0: // CRUD
				if (crudPanel != null) {
					System.out.println("Limpiando recursos CRUD...");
					tabbedPane.setComponentAt(0, null);
					crudController = null;
					crudPanel = null;
				}
				break;

			case 1: // FTP
				if (ftpService != null) {
					System.out.println("Cerrando recursos FTP...");
					ftpService.disconnectNotifications();
					ftpService.close();
					tabbedPane.setComponentAt(1, null);
					ftpService = null;
					ftpPanel = null;
				}
				break;

			case 2: // SMTP
				if (smtpPanel != null) {
					System.out.println("Limpiando recursos SMTP...");
					tabbedPane.setComponentAt(2, null);
					smtpPanel = null;
				}
				break;
		}
	}

	/**
	 * Switch to a specific tab by index
	 */
	public void switchToTab(int tabIndex) {
		if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(tabIndex);
		}
	}

}

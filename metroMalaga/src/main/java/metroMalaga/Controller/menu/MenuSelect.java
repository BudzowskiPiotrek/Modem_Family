package metroMalaga.Controller.menu;

import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.CrudController;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.*;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.View.*;

public class MenuSelect implements ChangeListener {
	private final PanelMenu panelMenu;
	private final JTabbedPane tabbedPane;
	private final Usuario user;

	private int previousTabIndex = -1;

	private CrudFrontend crudPanel;
	private PanelFTP ftpPanel;
	private PanelSMTP smtpPanel;
	
	private ServiceFTP ftpService;
	private CrudController crudController;

	public MenuSelect(PanelMenu panelMenu, JTabbedPane tabbedPane, Usuario user) {
		this.panelMenu = panelMenu;
		this.tabbedPane = tabbedPane;
		this.user = user;
		tabbedPane.addChangeListener(this);
	}

	public Usuario getUser() { return user; }

	public void updateActivePanelTheme() {
		
		if (crudPanel != null && tabbedPane.getSelectedComponent() == crudPanel) {
			crudPanel.applyTheme();
		} 
		else if (ftpPanel != null && tabbedPane.getSelectedComponent() == ftpPanel) {
			ftpPanel.applyTheme(); 
		} 
		else if (smtpPanel != null && tabbedPane.getSelectedComponent() == smtpPanel) {
			smtpPanel.applyTheme(); 
		}
	}
	
	public void updateActivePanelText() {
		if (crudPanel != null) crudPanel.updateAllTexts();
		if (ftpPanel != null) ftpPanel.updateAllTexts();
		if (smtpPanel != null) smtpPanel.updateAllTexts();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int selectedIndex = tabbedPane.getSelectedIndex();

		if (previousTabIndex != -1 && previousTabIndex != selectedIndex && previousTabIndex != 3) {
			cleanupPanel(previousTabIndex);
		}

		switch (selectedIndex) {
			case 0: initializeCRUD(); break;
			case 1: initializeFTP(); break;
			case 2: initializeSMTP(); break;
			case 3: break;
		}
		previousTabIndex = selectedIndex;
	}

	private void initializeCRUD() {
		System.out.println("Init CRUD...");
		crudPanel = new CrudFrontend(user);
		crudController = new CrudController(crudPanel, this);
		tabbedPane.setComponentAt(0, crudPanel);
	}

	private void initializeFTP() {
		System.out.println("Inicializando panel FTP...");

		// Create and show loading dialog immediately
		final LoadingDialog loadingDialog = new LoadingDialog();
		loadingDialog.showDialog();

		// Connect to FTP server in background thread
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			private ServiceFTP service;
			private FTPTableModel model;
			private PanelFTP panel;

			@Override
			protected Void doInBackground() throws Exception {
				publish("Conectando al servidor FTP...");
				service = new ServiceFTP();

				publish("Listando archivos...");
				FTPFile[] fileArray = service.listAllFiles();
				List<FTPFile> initialFiles = Arrays.asList(fileArray);

				publish("Inicializando sistema de notificaciones...");
				model = new FTPTableModel(initialFiles, service);
				service.setTableModel(model);

				publish("Configurando interfaz...");
				panel = new PanelFTP(user, service, initialFiles, model);

				new FTPlist(panel.getSearchField(), model);
				new FTPbtnUpFile(panel.getUploadButton(), service, model, user);
				new FTPbtnUp(panel.getUpButton(), service, model);
				new FTPdoubleClick(panel.getFileTable(), service, model);
				new FTPbtnReturn(panel, panel.getReturnButton(), user, MenuSelect.this);
				new FTPbtnNewFolder(panel.getFolderButton(), service, model, user);

				return null;
			}

			@Override
			protected void process(java.util.List<String> chunks) {
				// Update loading dialog with current status
				for (String status : chunks) {
					loadingDialog.updateStatus(status);
				}
			}

			@Override
			protected void done() {
				try {
					get(); // Check for exceptions

					// Close loading dialog
					loadingDialog.closeDialog();

					// Set the FTP panel in the UI
					ftpService = service;
					ftpPanel = panel;
					tabbedPane.setComponentAt(1, ftpPanel);

					System.out.println("Panel FTP inicializado correctamente");
				} catch (Exception e) {
					loadingDialog.closeDialog();
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Error al conectar con el servidor FTP:\n" + e.getMessage(),
							"Error de Conexión",
							JOptionPane.ERROR_MESSAGE);
					// Revert to previous tab
					tabbedPane.setSelectedIndex(previousTabIndex);
				}
			}
		};

		worker.execute();
	}

	private void initializeSMTP() {
		System.out.println("Init SMTP...");
		smtpPanel = new PanelSMTP(user);
		
		smtpPanel.setOnReturnCallback(() -> switchToTab(0));
		
		tabbedPane.setComponentAt(2, smtpPanel);
	}

	private void cleanupPanel(int tabIndex) {
		switch (tabIndex) {
			case 0:
				if (crudPanel != null) {
					System.out.println("Cleaning CRUD...");
					tabbedPane.setComponentAt(0, null);
					crudController = null;
					crudPanel = null;
				}
				break;
			case 1:
				if (ftpService != null) {
					System.out.println("Cleaning FTP...");
					ftpService.disconnectNotifications();
					ftpService.close();
					tabbedPane.setComponentAt(1, null);
					ftpService = null;
					ftpPanel = null;
				}
				break;
			case 2:
				if (smtpPanel != null) {
					System.out.println("Cleaning SMTP...");
					tabbedPane.setComponentAt(2, null); 
					smtpPanel = null;
				}
				break;
		}
	}

	public void switchToTab(int tabIndex) {
		if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(tabIndex);
		}
	}
}
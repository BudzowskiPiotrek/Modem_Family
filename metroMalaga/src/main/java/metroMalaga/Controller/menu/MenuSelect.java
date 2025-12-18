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
import metroMalaga.View.LoadingDialog;
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
				service = new ServiceFTP(user.getRol().getPermiso());

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

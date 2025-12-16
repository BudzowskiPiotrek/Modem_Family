package metroMalaga.View;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.FTPButtonsEditor;
import metroMalaga.Controller.FTPButtonsRenderer;
import metroMalaga.Controller.FTPbtnRefresh;
import metroMalaga.Controller.FTPbtnReturn;
import metroMalaga.Controller.FTPbtnUp;
import metroMalaga.Controller.FTPbtnUpFile;
import metroMalaga.Controller.FTPdoubleClick;
import metroMalaga.Controller.FTPlist;
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;

public class PanelFTP extends JFrame {
	private Usuario user;
	private FTPTableModel ftpModel;
	private JTable fileTable;
	private JTextField searchField;
	private JButton uploadButton, upButton, returnButton, reloadButton;
	private ServiceFTP service;

	public PanelFTP(Usuario user, ServiceFTP service, List<FTPFile> initialFiles) {
		this.service = service;
		this.ftpModel = new FTPTableModel(initialFiles, service);
		initializeComponents();
		attachListeners();
		setupFrameConfiguration();
		setupLayout();
	}

	private void initializeComponents() {
		this.fileTable = new JTable(this.ftpModel);
		this.searchField = new JTextField(20);
		this.uploadButton = new JButton("⬆️");
		this.upButton = new JButton("🔙");
		this.returnButton = new JButton("Return");
		this.reloadButton = new JButton("↻");
		FTPButtonsEditor buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel);
		fileTable.getColumnModel().getColumn(3).setCellRenderer(new FTPButtonsRenderer());
		fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
		fileTable.setRowHeight(30);
	}

	private void attachListeners() {
		FTPlist listener = new FTPlist(searchField, ftpModel);
		FTPbtnUpFile listenerFile = new FTPbtnUpFile(uploadButton, service, ftpModel);
		FTPbtnUp listenerUp = new FTPbtnUp(upButton, service, ftpModel);
		FTPdoubleClick listenerClick = new FTPdoubleClick(fileTable, service, ftpModel);
		FTPbtnReturn listenerReturMenu = new FTPbtnReturn(this, returnButton, user);
		FTPbtnRefresh listenerRefresh = new FTPbtnRefresh(reloadButton, service, ftpModel);
	}

	private void setupFrameConfiguration() {
		this.setTitle("FTP Manager - "); // + user.getNombre()
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	private void setupLayout() {
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanel.add(this.uploadButton);
		actionPanel.add(this.upButton);
		actionPanel.add(this.returnButton);
		actionPanel.add(this.reloadButton);

		JPanel filterPanel = new JPanel();
		filterPanel.add(new JLabel("Filtrar:"));
		filterPanel.add(this.searchField);

		JPanel tableContainer = new JPanel(new BorderLayout());
		tableContainer.add(new JScrollPane(fileTable), BorderLayout.CENTER);
		tableContainer.add(actionPanel, BorderLayout.SOUTH);

		this.add(tableContainer, BorderLayout.CENTER);
		this.add(filterPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}

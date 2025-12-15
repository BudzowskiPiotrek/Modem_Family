package metroMalaga.frontend.ftp;

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

import metroMalaga.Clases.FTPTableModel;
import metroMalaga.Clases.Usuario;
import metroMalaga.backend.FTPButtonsEditor;
import metroMalaga.backend.FTPbtnReturn;
import metroMalaga.backend.FTPbtnUp;
import metroMalaga.backend.FTPbtnUpFile;
import metroMalaga.backend.FTPdoubleClick;
import metroMalaga.backend.FTPlist;
import metroMalaga.backend.ServiceFTP;

public class PanelFTP extends JFrame {
	private Usuario user;
	private FTPTableModel ftpModel;
	private JTable fileTable;
	private JTextField searchField;
	private JButton uploadButton, upButton, returnButton;
	private ServiceFTP service;

	public PanelFTP(Usuario user) {
		service = new ServiceFTP("readwrite");// + user.getNombre()
		FTPFile[] fileArray = service.listAllFiles();
		List<FTPFile> initialFiles = new ArrayList<>(Arrays.asList(fileArray));
		this.ftpModel = new FTPTableModel(initialFiles);

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
		FTPButtonsEditor buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel);
		fileTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonsRenderer());
		fileTable.getColumnModel().getColumn(2).setCellEditor(buttonsEditor);
		fileTable.setRowHeight(30);
	}

	private void attachListeners() {
		FTPlist listener = new FTPlist(searchField, ftpModel);
		FTPbtnUpFile listenerFile = new FTPbtnUpFile(uploadButton, service, ftpModel);
		FTPbtnUp listenerUp = new FTPbtnUp(upButton, service, ftpModel);
		FTPdoubleClick listenerClick = new FTPdoubleClick(fileTable, service, ftpModel);
		FTPbtnReturn listenerReturMenu = new FTPbtnReturn(this, returnButton, user);
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

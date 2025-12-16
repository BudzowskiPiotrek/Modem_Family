package metroMalaga.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

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
	

	private static final Color ACCENT_RED = new Color(220, 53, 69);
	private static final Color BACKGROUND_LIGHT = Color.WHITE;
	private static final Color HEADER_GRAY = new Color(248, 249, 250);

	public PanelFTP(Usuario user, ServiceFTP service, List<FTPFile> initialFiles) {
		this.service = service;
		this.ftpModel = new FTPTableModel(initialFiles, service);
		initializeComponents();
		applyStyle();
		attachListeners();
		setupFrameConfiguration();
		setupLayout();
	}

	private void applyStyle() {
		Font modernFont = new Font("Dialog", Font.PLAIN, 14);

		// Marco de la ventana
		this.getContentPane().setBackground(BACKGROUND_LIGHT);

		// Campo de búsqueda
		searchField.setFont(modernFont);
		searchField.setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding interno

		// --- 2. Estilo de la JTable (FTP Manager) ---
		fileTable.setBackground(BACKGROUND_LIGHT);
		fileTable.setFont(modernFont);
		fileTable.setGridColor(HEADER_GRAY); // Líneas de la tabla más suaves
		fileTable.setSelectionBackground(new Color(230, 245, 255)); // Selección más suave

		// Encabezado de la tabla
		JTableHeader header = fileTable.getTableHeader();
		header.setFont(modernFont.deriveFont(Font.BOLD, 14));
		header.setBackground(HEADER_GRAY);
		header.setForeground(Color.BLACK);
		header.setBorder(null);

		// Centrar el contenido de las celdas (opcional para un diseño más limpio)
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < fileTable.getColumnCount() - 1; i++) {
			fileTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// --- 3. Estilo de Botones ---
		styleButton(uploadButton, ACCENT_RED, Color.WHITE);
		styleButton(upButton, Color.GRAY, Color.WHITE);
		styleButton(returnButton, ACCENT_RED, Color.WHITE);
		styleButton(reloadButton, new Color(108, 117, 125), Color.WHITE); // Gris oscuro

		// Aplicar la fuente moderna a los botones restantes y etiquetas
		uploadButton.setFont(modernFont);
		upButton.setFont(modernFont);
		returnButton.setFont(modernFont);
		reloadButton.setFont(modernFont);																			
	}
	private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
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

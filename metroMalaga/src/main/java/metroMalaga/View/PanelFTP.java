package metroMalaga.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.Common; // Importante
import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.FTPButtonsEditor;
import metroMalaga.Controller.ftp.FTPButtonsRenderer;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

public class PanelFTP extends JPanel {
	private Usuario user;
	private FTPTableModel ftpModel;
	private JTable fileTable;
	private JTextField searchField;
	private JButton uploadButton, upButton, returnButton, folderButton;
	private JLabel lblFilter;
	private ServiceFTP service;
	private FTPButtonsEditor buttonsEditor;
	private FTPButtonsRenderer buttonsRenderer;

	// Componentes promovidos a atributos para cambiar color
	private JPanel actionPanel, filterPanel, topPanel;
	private JScrollPane scrollPane;

	public PanelFTP(Usuario user, ServiceFTP service, List<FTPFile> initialFiles, FTPTableModel ftpModel) {
		this.service = service;
		this.ftpModel = ftpModel;
		this.user = user;
		
		initializeComponents();
		setupLayout(); // Configuramos layout primero para crear los paneles
		applyStyle();  // Estilos fijos (fuentes)
		applyTheme();  // Colores dinámicos
	}

	public void updateAllTexts() {
		returnButton.setText(Language.get(84));
		folderButton.setText("📁 " + Language.get(85));
		lblFilter.setText(Language.get(86));
		
		ftpModel.updateColumnNames();
		
		restoreButtonColumn();
		
		revalidate();
		repaint();
	}

	/**
	 * Método para aplicar el modo oscuro/claro usando Common
	 */
	public void applyTheme() {
		Color bgMain = Common.getBackground();
		Color bgPanel = Common.getPanelBackground();
		Color txt = Common.getText();
		Color fieldBg = Common.getFieldBackground();
		Color border = Common.getBorder();

		// Fondo principal
		this.setBackground(bgMain);
		
		// Paneles superiores
		if (actionPanel != null) actionPanel.setBackground(bgPanel);
		if (filterPanel != null) filterPanel.setBackground(bgPanel);
		if (topPanel != null) topPanel.setBackground(bgPanel);
		
		// Etiquetas
		if (lblFilter != null) lblFilter.setForeground(txt);
		
		// Campo de búsqueda
		if (searchField != null) {
			searchField.setBackground(fieldBg);
			searchField.setForeground(txt);
			searchField.setCaretColor(txt);
			searchField.setBorder(new LineBorder(border, 1));
		}

		// Tabla
		if (fileTable != null) {
			fileTable.setBackground(fieldBg);
			fileTable.setForeground(txt);
			fileTable.setGridColor(Common.isDarkMode ? new Color(60,60,60) : new Color(240, 240, 240));
			
			JTableHeader header = fileTable.getTableHeader();
			header.setBackground(Common.isDarkMode ? new Color(45,45,45) : new Color(248, 249, 250));
			header.setForeground(txt);
		}
		
		if (scrollPane != null) {
			scrollPane.getViewport().setBackground(bgMain);
		}

		// Botones (Mantenemos sus colores distintivos pero ajustamos si es necesario)
		// Upload y Return son rojos (Danger)
		styleButton(uploadButton, Common.getDanger(), Color.WHITE);
		styleButton(returnButton, Common.getDanger(), Color.WHITE);
		
		// Up es gris
		styleButton(upButton, Color.GRAY, Color.WHITE);
		
		// Folder es verde (podemos dejarlo fijo o usar un verde adaptado)
		styleButton(folderButton, new Color(40, 167, 69), Color.WHITE);

		this.repaint();
		this.revalidate();
	}

	private void restoreButtonColumn() {
		fileTable.getColumnModel().getColumn(3).setCellRenderer(buttonsRenderer);
		fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(150);
	}

	private void initializeComponents() {
		this.fileTable = new JTable(this.ftpModel);
		this.searchField = new JTextField(20);
		this.uploadButton = new JButton("⤒");
		this.upButton = new JButton("🔙");
		this.returnButton = new JButton(Language.get(84));
		this.folderButton = new JButton("📁 " + Language.get(85));
		this.lblFilter = new JLabel(Language.get(86));
		
		buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel, user);
		buttonsRenderer = new FTPButtonsRenderer();
		
		fileTable.getColumnModel().getColumn(3).setCellRenderer(buttonsRenderer);
		fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
		fileTable.setRowHeight(30);
	}

	private void applyStyle() {
		Font modernFont = new Font("Dialog", Font.PLAIN, 14);
		
		searchField.setFont(modernFont);
		// El borde del searchField se maneja en applyTheme

		fileTable.setFont(modernFont);
		fileTable.setSelectionBackground(new Color(230, 245, 255)); // O usar Common.getAccent()

		JTableHeader header = fileTable.getTableHeader();
		header.setFont(modernFont.deriveFont(Font.BOLD, 14));
		header.setBorder(null);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < fileTable.getColumnCount() - 1; i++) {
			fileTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		folderButton.setFont(modernFont);
		uploadButton.setFont(modernFont);
		upButton.setFont(modernFont);
		returnButton.setFont(modernFont);
	}

	private void styleButton(JButton button, Color background, Color foreground) {
		if (button == null) return;
		button.setBackground(background);
		button.setForeground(foreground);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(8, 15, 8, 15));
	}

	private void setupLayout() {
		setLayout(new BorderLayout());

		actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		actionPanel.add(this.uploadButton);
		actionPanel.add(this.upButton);
		actionPanel.add(this.folderButton);
		actionPanel.add(this.returnButton);
		
		filterPanel = new JPanel();
		filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		filterPanel.add(lblFilter);
		filterPanel.add(this.searchField);
		filterPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

		topPanel = new JPanel(new BorderLayout());
		topPanel.add(actionPanel, BorderLayout.WEST);
		topPanel.add(filterPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.SOUTH);

		scrollPane = new JScrollPane(fileTable);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
	}

	public JButton getFolderButton() {
		return folderButton;
	}

	public JTable getFileTable() {
		return fileTable;
	}

	public JTextField getSearchField() {
		return searchField;
	}

	public JButton getUploadButton() {
		return uploadButton;
	}

	public JButton getUpButton() {
		return upButton;
	}

	public JButton getReturnButton() {
		return returnButton;
	}
}
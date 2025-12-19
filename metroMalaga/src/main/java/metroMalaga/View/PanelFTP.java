package metroMalaga.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.Common;
import metroMalaga.Controller.ftp.FTPButtonsEditor;
import metroMalaga.Controller.ftp.FTPButtonsRenderer;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

/**
 * Panel for FTP file management.
 */
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

	private JPanel actionPanel, filterPanel, topPanel;
	private JScrollPane scrollPane;

	private static final Font MODERN_FONT = new Font("Dialog", Font.PLAIN, 14);
	private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 14);

	/**
	 * Constructor for PanelFTP.
	 * 
	 * @param user         The logged-in user.
	 * @param service      The FTP service instance.
	 * @param initialFiles Initial list of files to display.
	 * @param ftpModel     The table model for FTP files.
	 */
	public PanelFTP(Usuario user, ServiceFTP service, List<FTPFile> initialFiles, FTPTableModel ftpModel) {
		this.service = service;
		this.ftpModel = ftpModel;
		this.user = user;
		initializeComponents();
		setupLayout();
		applyTheme();
	}

	/**
	 * Applies the current application theme to the panel components.
	 */
	public void applyTheme() {
		Color bgMain = Common.getBackground();
		Color bgPanel = Common.getPanelBackground();
		Color txt = Common.getText();
		Color border = Common.getBorder();
		Color fieldBg = Common.getFieldBackground();
		Color accent = Common.getAccent();
		Color danger = Common.getDanger();

		// Fondo principal
		setBackground(bgMain);

		// Campo de búsqueda
		if (searchField != null) {
			searchField.setBackground(fieldBg);
			searchField.setForeground(txt);
			searchField.setCaretColor(txt);
			searchField.setBorder(new CompoundBorder(
					new LineBorder(border, 1),
					new EmptyBorder(5, 10, 5, 10)));
		}

		// Label
		if (lblFilter != null) {
			lblFilter.setForeground(txt);
			lblFilter.setFont(MODERN_FONT);
		}

		// Tabla
		if (fileTable != null) {
			fileTable.setBackground(fieldBg);
			fileTable.setForeground(txt);
			fileTable.setGridColor(Common.isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240));
			fileTable.setSelectionBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 245, 255));
			fileTable.setSelectionForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);
			fileTable.setShowVerticalLines(false);

			// 👇 RENDERER PARA CELDAS NORMALES
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
				@Override
				public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);

					if (!isSelected) {
						c.setBackground(Common.getFieldBackground());
						c.setForeground(Common.getText());
					} else {
						c.setBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 245, 255));
						c.setForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);
					}

					setHorizontalAlignment(JLabel.CENTER);
					setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
					return c;
				}
			};

			for (int i = 0; i < fileTable.getColumnCount() - 1; i++) {
				fileTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}

			// Header de la tabla
			JTableHeader header = fileTable.getTableHeader();
			header.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(248, 249, 250));
			header.setForeground(txt);
			header.setBorder(null);

			header.setDefaultRenderer(new DefaultTableCellRenderer() {
				@Override
				public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					l.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(248, 249, 250));
					l.setFont(HEADER_FONT);
					l.setForeground(Common.getText());
					l.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, Common.getBorder()));
					l.setHorizontalAlignment(JLabel.CENTER);
					return l;
				}
			});
		}

		// ScrollPane
		if (scrollPane != null) {
			scrollPane.setBorder(new LineBorder(border, 1));
			scrollPane.getViewport().setBackground(fieldBg);
			scrollPane.setBackground(fieldBg);
		}

		// Paneles
		if (actionPanel != null)
			actionPanel.setBackground(bgPanel);
		if (filterPanel != null)
			filterPanel.setBackground(bgPanel);
		if (topPanel != null)
			topPanel.setBackground(bgPanel);

		// 👇 BOTONES CON TAMAÑO CORRECTO
		if (uploadButton != null) {
			uploadButton.setBackground(bgPanel);
			uploadButton.setForeground(accent);
			uploadButton.setBorder(new CompoundBorder(
					new LineBorder(accent, 1),
					new EmptyBorder(8, 15, 8, 15)));
			uploadButton.setPreferredSize(new Dimension(80, 35));
		}

		if (upButton != null) {
			upButton.setBackground(bgPanel);
			upButton.setForeground(txt);
			upButton.setBorder(new CompoundBorder(
					new LineBorder(border, 1),
					new EmptyBorder(8, 15, 8, 15)));
			upButton.setPreferredSize(new Dimension(80, 35));
		}

		if (returnButton != null) {
			returnButton.setBackground(danger);
			returnButton.setForeground(Color.WHITE);
			returnButton.setBorder(new CompoundBorder(
					new LineBorder(danger, 1),
					new EmptyBorder(8, 15, 8, 15)));
		}

		if (folderButton != null) {
			folderButton.setBackground(Common.isDarkMode ? new Color(40, 167, 69) : new Color(40, 167, 69));
			folderButton.setForeground(Color.WHITE);
			folderButton.setBorder(new CompoundBorder(
					new LineBorder(new Color(40, 167, 69), 1),
					new EmptyBorder(8, 15, 8, 15)));
		}

		revalidate();
		repaint();
	}

	/**
	 * Updates all text labels based on the current language.
	 */
	public void updateAllTexts() {
		returnButton.setText(Language.get(84));
		folderButton.setText("📁 " + Language.get(85));
		lblFilter.setText(Language.get(86));

		ftpModel.updateColumnNames();

		restoreButtonColumn();

		applyTheme();
	}

	/**
	 * Restores the button column renderer and editor.
	 */
	private void restoreButtonColumn() {
		fileTable.getColumnModel().getColumn(3).setCellRenderer(buttonsRenderer);
		fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(200);
	}

	/**
	 * Initializes the UI components.
	 */
	private void initializeComponents() {
		this.fileTable = new JTable(this.ftpModel);
		this.searchField = new JTextField(20);
		this.uploadButton = new JButton("⤒");
		this.upButton = new JButton("🔙");
		this.returnButton = new JButton(Language.get(84));
		this.folderButton = new JButton("📁 " + Language.get(85));
		this.lblFilter = new JLabel(Language.get(86));

		buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel, user);
		buttonsRenderer = new FTPButtonsRenderer(user);

		fileTable.getColumnModel().getColumn(3).setCellRenderer(buttonsRenderer);
		fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
		fileTable.setRowHeight(40); // 👈 Altura de fila aumentada

		searchField.setFont(MODERN_FONT);
		fileTable.setFont(MODERN_FONT);

		uploadButton.setFont(MODERN_FONT);
		upButton.setFont(MODERN_FONT);
		returnButton.setFont(MODERN_FONT);
		folderButton.setFont(MODERN_FONT);

		uploadButton.setFocusPainted(false);
		upButton.setFocusPainted(false);
		returnButton.setFocusPainted(false);
		folderButton.setFocusPainted(false);

		uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		upButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		folderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Sets up the layout of the panel.
	 */
	private void setupLayout() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(15, 15, 15, 15));

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

		topPanel = new JPanel(new BorderLayout());
		topPanel.add(actionPanel, BorderLayout.WEST);
		topPanel.add(filterPanel, BorderLayout.EAST);

		add(topPanel, BorderLayout.SOUTH);

		scrollPane = new JScrollPane(fileTable);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Gets the folder button.
	 * 
	 * @return The folder button.
	 */
	public JButton getFolderButton() {
		return folderButton;
	}

	/**
	 * Gets the file table.
	 * 
	 * @return The file table.
	 */
	public JTable getFileTable() {
		return fileTable;
	}

	/**
	 * Gets the search field.
	 * 
	 * @return The search field.
	 */
	public JTextField getSearchField() {
		return searchField;
	}

	/**
	 * Gets the upload button.
	 * 
	 * @return The upload button.
	 */
	public JButton getUploadButton() {
		return uploadButton;
	}

	/**
	 * Gets the up button.
	 * 
	 * @return The up button.
	 */
	public JButton getUpButton() {
		return upButton;
	}

	/**
	 * Gets the return button.
	 * 
	 * @return The return button.
	 */
	public JButton getReturnButton() {
		return returnButton;
	}
}

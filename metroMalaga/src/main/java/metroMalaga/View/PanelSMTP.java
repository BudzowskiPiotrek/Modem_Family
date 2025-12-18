package metroMalaga.View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;
import metroMalaga.Controller.Common;
import metroMalaga.Controller.smtp.ButtonHandleSMTP;
import metroMalaga.Controller.smtp.ButtonHoverHandle;
import metroMalaga.Controller.smtp.FieldFocusHandle;
import metroMalaga.Controller.smtp.HandleSMTP;

public class PanelSMTP extends JPanel {

	private final HandleSMTP backend;
	private final Usuario loggedUser;

	// Componentes UI
	private JTextField txtTo, txtSubject;
	private JTextArea txtBody, txtViewer;
	private JTable emailTable;
	private DefaultTableModel tableModel;
	private JButton btnSend, btnAttach, btnClearAttach, btnRefresh, btnDelete, btnToggleRead, btnDownloadEmail, btnReturn;
	private JLabel lblAttachedFile, lblTo, lblSubject, lblFolder;
	private JComboBox<String> cboFolders;
	
	// Paneles
	private JPanel pCompose, pInbox, pButtonsCompose, pButtonsInbox, pFields, pFolderSelector, pInboxTop;
	private JSplitPane mainSplit, inboxSplit;
	
	private ButtonHandleSMTP buttonHandler;

	// Fuentes
	private final Font F_HEADER = new Font("Segoe UI", Font.BOLD, 16);
	private final Font F_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

	public PanelSMTP(Usuario usuario) {
		this.loggedUser = usuario;
		this.backend = new HandleSMTP();
		backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

		setupPanel();
		initUI();
		registerListeners();
		
		// Aplicar tema inicial
		applyTheme();
	}

	private void setupPanel() {
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(15, 15, 15, 15));
	}

	public void applyTheme() {
		Color bgMain = Common.getBackground();
		Color bgPanel = Common.getPanelBackground();
		Color txt = Common.getText();
		Color border = Common.getBorder();
		Color fieldBg = Common.getFieldBackground();

		// 1. Fondos principales
		this.setBackground(bgMain);
		if (mainSplit != null) mainSplit.setBackground(bgMain);
		if (inboxSplit != null) inboxSplit.setBackground(bgMain);

		// 2. Paneles
		updatePanelStyle(pCompose, Language.get(53));
		updatePanelStyle(pInbox, Language.get(54));
		
		if (pFields != null) pFields.setBackground(bgPanel);
		if (pButtonsCompose != null) pButtonsCompose.setBackground(bgPanel);
		if (pButtonsInbox != null) pButtonsInbox.setBackground(bgPanel);
		if (pFolderSelector != null) pFolderSelector.setBackground(bgPanel);
		if (pInboxTop != null) pInboxTop.setBackground(bgPanel);

		// 3. Etiquetas
		if (lblTo != null) lblTo.setForeground(txt);
		if (lblSubject != null) lblSubject.setForeground(txt);
		if (lblFolder != null) lblFolder.setForeground(txt);
		
		if (lblAttachedFile != null) {
			if (lblAttachedFile.getText().contains(Language.get(60)) || lblAttachedFile.getText().contains("No ")) {
				lblAttachedFile.setForeground(Color.GRAY);
			} else {
				lblAttachedFile.setForeground(txt);
			}
		}

		// 4. Campos y Combo
		updateFieldStyle(txtTo);
		updateFieldStyle(txtSubject);
		updateFieldStyle(txtBody);
		updateFieldStyle(txtViewer);
		
		if (cboFolders != null) {
			cboFolders.setBackground(fieldBg);
			cboFolders.setForeground(txt);
			cboFolders.setBorder(new LineBorder(border, 1));
		}

		// 5. Tabla
		if (emailTable != null) {
			emailTable.setBackground(fieldBg);
			emailTable.setForeground(txt);
			emailTable.setGridColor(Common.isDarkMode ? new Color(60,60,60) : new Color(240, 240, 240));
			JTableHeader header = emailTable.getTableHeader();
			header.setBackground(Common.isDarkMode ? new Color(45,45,45) : new Color(245, 245, 245));
			header.setForeground(txt);
			header.setBorder(new MatteBorder(0, 0, 1, 0, border));
		}

		// 6. Botones
		updateButtonsTheme();

		this.repaint();
		this.revalidate();
	}

	private void updatePanelStyle(JPanel p, String title) {
		if (p == null) return;
		p.setBackground(Common.getPanelBackground());
		p.setBorder(new CompoundBorder(
			new LineBorder(Common.getBorder(), 1), 
			new TitledBorder(new EmptyBorder(10, 10, 10, 10), title, TitledBorder.LEFT, TitledBorder.TOP, F_HEADER, Common.getText())
		));
	}

	private void updateFieldStyle(JTextComponent c) {
		if (c == null) return;
		c.setBackground(Common.getFieldBackground());
		c.setForeground(Common.getText());
		c.setCaretColor(Common.getText());
		if (c instanceof JTextField) {
			c.setBorder(new CompoundBorder(new LineBorder(Common.getBorder(), 1), new EmptyBorder(2, 5, 2, 5)));
		}
	}
	
	private void updateButtonsTheme() {
		Color bg = Common.getPanelBackground();
		Color txt = Common.getText();
		Color danger = Common.getDanger();
		Color accent = Common.getAccent();
		
		JButton[] normalButtons = {btnAttach, btnRefresh, btnToggleRead, btnDownloadEmail};
		for (JButton b : normalButtons) {
			if (b != null) {
				b.setBackground(bg);
				b.setForeground(txt);
				b.setBorder(new LineBorder(Common.getBorder(), 1));
			}
		}
		
		if (btnSend != null) {
			btnSend.setBackground(bg);
			btnSend.setForeground(accent);
			btnSend.setBorder(new LineBorder(accent, 1, true));
		}
		
		JButton[] dangerButtons = {btnClearAttach, btnDelete, btnReturn};
		for (JButton b : dangerButtons) {
			if (b != null) {
				b.setBackground(danger);
				b.setForeground(Color.WHITE);
				b.setBorder(new LineBorder(danger, 1));
			}
		}
	}

	public void updateAllTexts() {
		lblTo.setText(Language.get(55));
		lblSubject.setText(Language.get(56));
		
		// Nuevo texto para carpeta (asegúrate de tener ID 197 en Language o cambia el ID)
		if (lblFolder != null) lblFolder.setText(Language.get(197)); 

		String currentLabel = lblAttachedFile.getText();
		if (currentLabel.contains("No ") || currentLabel.contains("files") || currentLabel.contains(Language.get(60))) {
			lblAttachedFile.setText(Language.get(60));
		}

		btnAttach.setText(Language.get(57));
		btnClearAttach.setText(Language.get(58));
		btnSend.setText(Language.get(59));

		tableModel.setColumnIdentifiers(new String[]{
			Language.get(61), Language.get(62), Language.get(63)
		});

		btnRefresh.setText(Language.get(64));
		btnToggleRead.setText(Language.get(65));
		btnDownloadEmail.setText(Language.get(66));
		btnDelete.setText(Language.get(67));
		btnReturn.setText(Language.get(69));

		String viewerText = txtViewer.getText();
		if (viewerText.contains("Select") || viewerText.contains("Selecciona") || viewerText.contains(Language.get(68))) {
			txtViewer.setText(Language.get(68));
		}

		applyTheme(); 
	}

	private void initUI() {
		pCompose = new JPanel(new BorderLayout(10, 10));
		pFields = new JPanel(new GridBagLayout());

		txtTo = createField();
		txtSubject = createField();
		lblTo = new JLabel(Language.get(55));
		lblSubject = new JLabel(Language.get(56));

		addGBC(pFields, lblTo, 0, 0, 0.05);
		addGBC(pFields, txtTo, 1, 0, 0.95);
		addGBC(pFields, lblSubject, 0, 1, 0.05);
		addGBC(pFields, txtSubject, 1, 1, 0.95);

		txtBody = createTextArea();
		JScrollPane scrollBody = createScrollPane(txtBody);

		pButtonsCompose = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		btnAttach = createButton(Language.get(57), false);
		btnClearAttach = createButton(Language.get(58), true);
		btnSend = createButton(Language.get(59), false);

		lblAttachedFile = new JLabel(Language.get(60));
		lblAttachedFile.setForeground(Color.GRAY);
		lblAttachedFile.setFont(F_TEXT);
		lblAttachedFile.setPreferredSize(new Dimension(250, 30));

		pButtonsCompose.add(btnAttach);
		pButtonsCompose.add(btnClearAttach);
		pButtonsCompose.add(lblAttachedFile);
		pButtonsCompose.add(Box.createHorizontalStrut(20));
		pButtonsCompose.add(btnSend);

		pCompose.add(pFields, BorderLayout.NORTH);
		pCompose.add(scrollBody, BorderLayout.CENTER);
		pCompose.add(pButtonsCompose, BorderLayout.SOUTH);

		// --- Panel Inbox ---
		pInbox = new JPanel(new BorderLayout(10, 10));

		// Selector de Carpetas
		pFolderSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		lblFolder = new JLabel(Language.get(197)); // "Folder:"
		lblFolder.setFont(F_TEXT);
		
		cboFolders = new JComboBox<>();
		cboFolders.setPreferredSize(new Dimension(200, 30));
		cboFolders.setFont(F_TEXT);
		
		// Carpetas Gmail estándar (puedes cargar dinámicamente si el backend lo soporta)
		cboFolders.addItem("INBOX");
		cboFolders.addItem("[Gmail]/Spam");
		cboFolders.addItem("[Gmail]/Trash");
		cboFolders.addItem("[Gmail]/Sent Mail");
		cboFolders.setSelectedItem("INBOX");
		
		pFolderSelector.add(lblFolder);
		pFolderSelector.add(cboFolders);

		// Tabla y Visor
		String[] cols = { Language.get(61), Language.get(62), Language.get(63) };
		tableModel = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) { return false; }
		};
		emailTable = createTable();

		txtViewer = createTextArea();
		txtViewer.setEditable(false);
		txtViewer.setText(Language.get(68));

		inboxSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createScrollPane(emailTable), createScrollPane(txtViewer));
		inboxSplit.setDividerLocation(500);
		inboxSplit.setDividerSize(5);
		inboxSplit.setBorder(null);

		// Panel superior del Inbox (Combo + Split)
		pInboxTop = new JPanel(new BorderLayout());
		pInboxTop.add(pFolderSelector, BorderLayout.NORTH);
		pInboxTop.add(inboxSplit, BorderLayout.CENTER);

		pButtonsInbox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

		btnRefresh = createButton(Language.get(64), false);
		btnToggleRead = createButton(Language.get(65), false);
		btnDownloadEmail = createButton(Language.get(66), false);
		btnDownloadEmail.setEnabled(false);
		btnDelete = createButton(Language.get(67), true);
		btnReturn = createButton(Language.get(69), true);

		pButtonsInbox.add(btnRefresh);
		pButtonsInbox.add(btnToggleRead);
		pButtonsInbox.add(btnDownloadEmail);
		pButtonsInbox.add(btnDelete);
		pButtonsInbox.add(btnReturn);

		pInbox.add(pInboxTop, BorderLayout.CENTER);
		pInbox.add(pButtonsInbox, BorderLayout.SOUTH);

		mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pCompose, pInbox);
		mainSplit.setDividerLocation(380);
		mainSplit.setBorder(null);
		mainSplit.setDividerSize(10);

		add(mainSplit, BorderLayout.CENTER);
	}

	private void registerListeners() {
		this.buttonHandler = new ButtonHandleSMTP(this, backend);

		// Listener para el Combo de Carpetas
		cboFolders.addActionListener(e -> {
			String selectedFolder = (String) cboFolders.getSelectedItem();
			if (selectedFolder != null) {
				// NOTA: Debes añadir el método setCurrentFolder en HandleSMTP
				backend.setCurrentFolder(selectedFolder); 
				buttonHandler.refreshInbox(false);
			}
		});


		// Hovers
		applyHover(btnAttach, false);
		applyHover(btnRefresh, false);
		applyHover(btnToggleRead, false);
		applyHover(btnDownloadEmail, false);
		
		applyHover(btnSend, false);
		applyHover(btnClearAttach, true);
		applyHover(btnDelete, true);
		applyHover(btnReturn, true);
	}

	private void applyHover(JButton btn, boolean isDanger) {
		Color bg = isDanger ? Common.getDanger() : Common.getPanelBackground();
		Color fg = isDanger ? Color.WHITE : Common.getText();
		Color hoverBg = Common.getHoverColor(isDanger, btn == btnSend);
		
		MatteBorder b = new MatteBorder(1, 1, 1, 1, isDanger ? Common.getDanger() : (btn == btnSend ? Common.getAccent() : Common.getBorder()));
		btn.setBorder(new CompoundBorder(b, new EmptyBorder(5, 10, 5, 10)));
		
		for(java.awt.event.MouseListener ml : btn.getMouseListeners()) {
			if (ml instanceof ButtonHoverHandle) btn.removeMouseListener(ml);
		}
		
		btn.addMouseListener(new ButtonHoverHandle(btn, bg, fg, hoverBg, fg, b, b));
	}

	// Métodos Auxiliares
	private void addGBC(JPanel p, Component c, int x, int y, double weight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x; gbc.gridy = y; gbc.weightx = weight;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(8, 5, 8, 5);
		if (c instanceof JLabel) {
			((JLabel) c).setFont(F_TEXT);
		}
		p.add(c, gbc);
	}

	private JTextField createField() {
		JTextField f = new JTextField();
		f.setPreferredSize(new Dimension(200, 30));
		f.setFont(F_TEXT);
		f.addFocusListener(new FieldFocusHandle(f, Common.getBorder(), Common.getAccent()));
		return f;
	}

	private JTextArea createTextArea() {
		JTextArea a = new JTextArea();
		a.setFont(F_TEXT);
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		a.setBorder(new EmptyBorder(5, 5, 5, 5));
		return a;
	}

	private JScrollPane createScrollPane(Component c) {
		return new JScrollPane(c);
	}

	private JButton createButton(String text, boolean isDanger) {
		JButton b = new JButton(text);
		b.setPreferredSize(new Dimension(140, 35));
		b.setFont(new Font("Segoe UI", Font.BOLD, 13));
		b.setFocusPainted(false);
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return b;
	}

	private JTable createTable() {
		JTable t = new JTable(tableModel);
		t.setFont(F_TEXT);
		t.setRowHeight(28);
		t.setSelectionBackground(new Color(230, 240, 255));
		t.setSelectionForeground(Color.BLACK);
		t.setShowVerticalLines(false);
		return t;
	}

	// Getters
	public JTextField getTxtTo() { return txtTo; }
	public JTextField getTxtSubject() { return txtSubject; }
	public JTextArea getTxtBody() { return txtBody; }
	public JTextArea getTxtViewer() { return txtViewer; }
	public JTable getEmailTable() { return emailTable; }
	public DefaultTableModel getTableModel() { return tableModel; }
	public JButton getBtnSend() { return btnSend; }
	public JButton getBtnAttach() { return btnAttach; }
	public JButton getBtnClearAttach() { return btnClearAttach; }
	public JButton getBtnRefresh() { return btnRefresh; }
	public JButton getBtnDelete() { return btnDelete; }
	public JButton getBtnToggleRead() { return btnToggleRead; }
	public JButton getBtnDownloadEmail() { return btnDownloadEmail; }
	public JLabel getLblAttachedFile() { return lblAttachedFile; }
	public JButton getBtnReturn() { return btnReturn; }
	public void setBtnReturn(JButton btnReturn) { this.btnReturn = btnReturn; }

	public void setOnReturnCallback(Runnable callback) {
		if (buttonHandler != null) {
			buttonHandler.setOnReturnCallback(callback);
		}
	}
}
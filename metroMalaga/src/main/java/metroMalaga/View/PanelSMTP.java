package metroMalaga.View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;

import java.awt.*;

import metroMalaga.Model.Usuario;
import metroMalaga.Controller.smtp.ButtonHandleSMTP;
import metroMalaga.Controller.smtp.ButtonHoverHandle;
import metroMalaga.Controller.smtp.FieldFocusHandle;
import metroMalaga.Controller.smtp.HandleSMTP;

public class PanelSMTP extends JFrame {

	private final HandleSMTP backend;
	private final Usuario loggedUser;

	private JTextField txtTo, txtSubject;
	private JTextArea txtBody, txtViewer;
	private JTable emailTable;
	private DefaultTableModel tableModel;
	private JButton btnSend, btnAttach, btnClearAttach, btnRefresh, btnDelete, btnToggleRead, btnDownloadEmail;
	private JLabel lblAttachedFile;

	private final Color BG_MAIN = new Color(245, 247, 250);
	private final Color BG_PANEL = Color.WHITE;
	private final Color TXT_DARK = new Color(50, 50, 50);
	private final Color C_BORDER = new Color(220, 220, 220);
	private final Color C_ACCENT = new Color(70, 130, 180);
	private final Color C_DANGER = new Color(220, 53, 69);
	private final Font F_HEADER = new Font("Segoe UI", Font.BOLD, 16);
	private final Font F_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

	public PanelSMTP(Usuario usuario) {
		this.loggedUser = usuario;
		this.backend = new HandleSMTP();
		backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

		setupFrame();
		initUI();
		registerListeners();
	}

	private void setupFrame() {
		setTitle("Email Manager - " + loggedUser.getUsernameApp());
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel content = new JPanel(new BorderLayout(15, 15));
		content.setBackground(BG_MAIN);
		content.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(content);
	}

	private void initUI() {
		JPanel pCompose = createStyledPanel("New Message");
		JPanel pFields = new JPanel(new GridBagLayout());
		pFields.setBackground(BG_PANEL);

		txtTo = createField();
		txtSubject = createField();

		addGBC(pFields, new JLabel("To:"), 0, 0, 0.05);
		addGBC(pFields, txtTo, 1, 0, 0.95);
		addGBC(pFields, new JLabel("Subject:"), 0, 1, 0.05);
		addGBC(pFields, txtSubject, 1, 1, 0.95);

		txtBody = createTextArea();
		JScrollPane scrollBody = createScrollPane(txtBody);

		JPanel pButtonsCompose = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		pButtonsCompose.setBackground(BG_PANEL);

		btnAttach = createButton("Attach File", false);
		btnClearAttach = createButton("Clear", true);
		btnSend = createButton("Send Email", false);
		btnSend.setBorder(new LineBorder(C_ACCENT, 1, true));
		btnSend.setForeground(C_ACCENT);

		lblAttachedFile = new JLabel("No files attached");
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

		JPanel pInbox = createStyledPanel("Inbox");

		String[] cols = { "Status", "Sender", "Subject" };
		tableModel = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		emailTable = createTable();

		txtViewer = createTextArea();
		txtViewer.setEditable(false);
		txtViewer.setText("Select an email to read...");

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createScrollPane(emailTable),
				createScrollPane(txtViewer));
		split.setDividerLocation(500);
		split.setDividerSize(5);
		split.setBorder(null);
		split.setBackground(BG_MAIN);

		JPanel pButtonsInbox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		pButtonsInbox.setBackground(BG_PANEL);

		btnRefresh = createButton("Refresh", false);
		btnToggleRead = createButton("Mark Read/Unread", false);
		btnDownloadEmail = createButton("Download .eml", false);
		btnDownloadEmail.setEnabled(false);
		btnDelete = createButton("Delete Email", true);

		pButtonsInbox.add(btnRefresh);
		pButtonsInbox.add(btnToggleRead);
		pButtonsInbox.add(btnDownloadEmail);
		pButtonsInbox.add(btnDelete);

		pInbox.add(split, BorderLayout.CENTER);
		pInbox.add(pButtonsInbox, BorderLayout.SOUTH);

		JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pCompose, pInbox);
		mainSplit.setDividerLocation(380);
		mainSplit.setBorder(null);
		mainSplit.setDividerSize(10);
		mainSplit.setBackground(BG_MAIN);

		add(mainSplit, BorderLayout.CENTER);
	}

	private void registerListeners() {
		new ButtonHandleSMTP(this, backend);

		applyHover(btnAttach, BG_PANEL, TXT_DARK, false);
		applyHover(btnRefresh, BG_PANEL, TXT_DARK, false);
		applyHover(btnToggleRead, BG_PANEL, TXT_DARK, false);
		applyHover(btnDownloadEmail, BG_PANEL, TXT_DARK, false);
		applyHover(btnSend, BG_PANEL, C_ACCENT, false);
		applyHover(btnClearAttach, C_DANGER, Color.WHITE, true);
		applyHover(btnDelete, C_DANGER, Color.WHITE, true);
	}

	private void addGBC(JPanel p, Component c, int x, int y, double weight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = weight;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(8, 5, 8, 5);
		if (c instanceof JLabel) {
			((JLabel) c).setFont(F_TEXT);
			((JLabel) c).setForeground(TXT_DARK);
		}
		p.add(c, gbc);
	}

	private JPanel createStyledPanel(String title) {
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(BG_PANEL);
		p.setBorder(new CompoundBorder(new LineBorder(C_BORDER, 1), new TitledBorder(new EmptyBorder(10, 10, 10, 10),
				title, TitledBorder.LEFT, TitledBorder.TOP, F_HEADER, TXT_DARK)));
		return p;
	}

	private JTextField createField() {
		JTextField f = new JTextField();
		f.setPreferredSize(new Dimension(200, 30));
		styleCommon(f);
		f.setBorder(new CompoundBorder(new LineBorder(C_BORDER, 1), new EmptyBorder(2, 5, 2, 5)));
		f.addFocusListener(new FieldFocusHandle(f, C_BORDER, C_ACCENT));
		return f;
	}

	private JTextArea createTextArea() {
		JTextArea a = new JTextArea();
		styleCommon(a);
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		a.setBorder(new EmptyBorder(5, 5, 5, 5));
		return a;
	}

	private void styleCommon(JTextComponent c) {
		c.setBackground(Color.WHITE);
		c.setForeground(TXT_DARK);
		c.setCaretColor(TXT_DARK);
		c.setFont(F_TEXT);
	}

	private JScrollPane createScrollPane(Component c) {
		JScrollPane s = new JScrollPane(c);
		s.setBorder(new LineBorder(C_BORDER, 1));
		s.setBackground(Color.WHITE);
		s.getViewport().setBackground(Color.WHITE);
		return s;
	}

	private JButton createButton(String text, boolean isDanger) {
		JButton b = new JButton(text);
		b.setPreferredSize(new Dimension(140, 35));
		b.setFont(new Font("Segoe UI", Font.BOLD, 13));
		b.setFocusPainted(false);
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		b.setBackground(isDanger ? C_DANGER : BG_PANEL);
		b.setForeground(isDanger ? Color.WHITE : TXT_DARK);
		b.setBorder(new LineBorder(isDanger ? C_DANGER : C_BORDER, 1));
		return b;
	}

	private JTable createTable() {
		JTable t = new JTable(tableModel);
		t.setBackground(Color.WHITE);
		t.setForeground(TXT_DARK);
		t.setFont(F_TEXT);
		t.setRowHeight(28);
		t.setGridColor(new Color(240, 240, 240));
		t.setSelectionBackground(new Color(230, 240, 255));
		t.setSelectionForeground(TXT_DARK);
		t.setShowVerticalLines(false);
		t.getTableHeader().setBackground(new Color(245, 245, 245));
		t.getTableHeader().setForeground(TXT_DARK);
		t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
		return t;
	}

	private void applyHover(JButton btn, Color bg, Color fg, boolean isDanger) {
		Color hoverBg = isDanger ? new Color(200, 40, 50) : new Color(240, 240, 240);
		if (fg.equals(C_ACCENT))
			hoverBg = new Color(235, 245, 255);

		MatteBorder b = new MatteBorder(1, 1, 1, 1, isDanger ? C_DANGER : (fg.equals(C_ACCENT) ? C_ACCENT : C_BORDER));
		btn.setBorder(new CompoundBorder(b, new EmptyBorder(5, 10, 5, 10)));
		btn.addMouseListener(new ButtonHoverHandle(btn, bg, fg, hoverBg, fg, b, b));
	}

	public JTextField getTxtTo() {
		return txtTo;
	}

	public JTextField getTxtSubject() {
		return txtSubject;
	}

	public JTextArea getTxtBody() {
		return txtBody;
	}

	public JTextArea getTxtViewer() {
		return txtViewer;
	}

	public JTable getEmailTable() {
		return emailTable;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnSend() {
		return btnSend;
	}

	public JButton getBtnAttach() {
		return btnAttach;
	}

	public JButton getBtnClearAttach() {
		return btnClearAttach;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnToggleRead() {
		return btnToggleRead;
	}

	public JButton getBtnDownloadEmail() {
		return btnDownloadEmail;
	}

	public JLabel getLblAttachedFile() {
		return lblAttachedFile;
	}
}
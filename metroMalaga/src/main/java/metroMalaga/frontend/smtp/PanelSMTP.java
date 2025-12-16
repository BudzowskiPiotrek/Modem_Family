package metroMalaga.frontend.smtp;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import metroMalaga.Clases.Usuario;
import metroMalaga.backend.smtp.HandleSMTP;
import metroMalaga.Clases.EmailModel;

public class PanelSMTP extends JFrame {

	private HandleSMTP backend;
	private Usuario loggedUser;
	private List<EmailModel> currentEmailList;

	private JTextField txtTo, txtSubject;
	private JTextArea txtBody, txtViewer;
	private JTable emailTable;
	private DefaultTableModel tableModel;

	private JButton btnSend, btnAttach, btnClearAttach;
	private JLabel lblAttachedFile;
	private List<File> attachmentsList = new ArrayList<>();

	private JButton btnRefresh, btnDelete, btnToggleRead;
	private JButton btnDownloadEmail;

	public PanelSMTP(Usuario usuario) {
		this.loggedUser = usuario;
		this.backend = new HandleSMTP();
		this.currentEmailList = new ArrayList<>();

		backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

		setTitle("Email Manager - " + usuario.getUsernameApp());
		setSize(950, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initComponents();
	}

	private void initComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// --- TOP: COMPOSE ---
		JPanel composePanel = new JPanel(new BorderLayout(5, 5));
		composePanel.setBorder(new TitledBorder("Compose"));

		JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
		fieldsPanel.add(new JLabel("To:"));
		txtTo = new JTextField();
		fieldsPanel.add(txtTo);
		fieldsPanel.add(new JLabel("Subject:"));
		txtSubject = new JTextField();
		fieldsPanel.add(txtSubject);

		txtBody = new JTextArea(3, 20);

		JPanel bottomComposePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnAttach = new JButton("Attach (+)");
		btnClearAttach = new JButton("Clear");
		btnClearAttach.setForeground(Color.RED);

		// Wider label to see names
		lblAttachedFile = new JLabel("No file selected");
		lblAttachedFile.setForeground(Color.GRAY);
		lblAttachedFile.setPreferredSize(new Dimension(300, 20));

		btnSend = new JButton("Send Email");

		bottomComposePanel.add(btnAttach);
		bottomComposePanel.add(btnClearAttach);
		bottomComposePanel.add(lblAttachedFile);
		bottomComposePanel.add(Box.createHorizontalStrut(10));
		bottomComposePanel.add(btnSend);

		composePanel.add(fieldsPanel, BorderLayout.NORTH);
		composePanel.add(new JScrollPane(txtBody), BorderLayout.CENTER);
		composePanel.add(bottomComposePanel, BorderLayout.SOUTH);

		// --- BOTTOM: INBOX ---
		JPanel inboxPanel = new JPanel(new BorderLayout(5, 5));
		inboxPanel.setBorder(new TitledBorder("Inbox"));

		String[] columns = { "Status", "Sender", "Subject" };
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		emailTable = new JTable(tableModel);
		emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		txtViewer = new JTextArea("Select an email...");
		txtViewer.setLineWrap(true);
		txtViewer.setEditable(false);
		txtViewer.setBackground(new Color(245, 245, 245));

		JSplitPane splitInbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(emailTable),
				new JScrollPane(txtViewer));
		splitInbox.setDividerLocation(500);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		btnRefresh = new JButton("Refresh");
		btnToggleRead = new JButton("Mark Read/Unread");
		btnDownloadEmail = new JButton("Download (.eml)");
		btnDownloadEmail.setEnabled(false);
		btnDelete = new JButton("Delete");
		btnDelete.setForeground(Color.RED);

		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnToggleRead);
		buttonPanel.add(btnDownloadEmail);
		buttonPanel.add(btnDelete);

		inboxPanel.add(splitInbox, BorderLayout.CENTER);
		inboxPanel.add(buttonPanel, BorderLayout.SOUTH);

		JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, composePanel, inboxPanel);
		mainSplit.setDividerLocation(300);
		mainPanel.add(mainSplit, BorderLayout.CENTER);
		setContentPane(mainPanel);

		setupEvents();
	}

	private void setupEvents() {

		btnAttach.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				attachmentsList.addAll(Arrays.asList(fileChooser.getSelectedFiles()));

				StringBuilder sb = new StringBuilder();
				for (File f : attachmentsList) {
					sb.append(f.getName()).append(", ");
				}
				String text = sb.toString();
				if (text.length() > 2)
					text = text.substring(0, text.length() - 2);
				if (text.length() > 40)
					text = text.substring(0, 40) + "...";

				lblAttachedFile.setText(text);
				lblAttachedFile.setForeground(new Color(0, 100, 0));
				lblAttachedFile.setToolTipText(sb.toString());
			}
		});

		btnClearAttach.addActionListener(e -> {
			attachmentsList.clear();
			lblAttachedFile.setText("No file selected");
			lblAttachedFile.setForeground(Color.GRAY);
			lblAttachedFile.setToolTipText(null);
		});

		btnSend.addActionListener(e -> {
			String recipient = txtTo.getText().trim();
			String subject = txtSubject.getText().trim();
			String body = txtBody.getText().trim();

			if (recipient.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Error: The 'To' field is required.", "Missing Data",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!recipient.contains("@") || !recipient.contains(".")) {
				JOptionPane.showMessageDialog(this, "Error: The recipient email does not seem valid.",
						"Incorrect Format", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (subject.isEmpty()) {
				int confirm = JOptionPane.showConfirmDialog(this,
						"The subject is empty. Do you want to send the email without a subject?", "Empty Subject",
						JOptionPane.YES_NO_OPTION);
				if (confirm != JOptionPane.YES_OPTION)
					return;
			}

			new Thread(() -> {
				try {
					btnSend.setEnabled(false);
					btnSend.setText("Sending...");
					backend.sendEmail(recipient, subject, body, attachmentsList);
					JOptionPane.showMessageDialog(this, "Email sent successfully.");
					txtTo.setText("");
					txtSubject.setText("");
					txtBody.setText("");
					attachmentsList.clear();
					lblAttachedFile.setText("No file selected");
					lblAttachedFile.setForeground(Color.GRAY);
					btnSend.setText("Send Email");
					btnSend.setEnabled(true);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error sending: " + ex.getMessage(), "Critical Error",
					JOptionPane.ERROR_MESSAGE);
					btnSend.setText("Send Email");
					btnSend.setEnabled(true);
				}
			}).start();
		});

		btnRefresh.addActionListener(e -> {
			new Thread(() -> {
				btnRefresh.setEnabled(false);
				tableModel.setRowCount(0);
				txtViewer.setText("Syncing with Gmail...");
				try {
					currentEmailList = backend.fetchEmails();
					for (EmailModel mail : currentEmailList) {
						String status = mail.isRead() ? "READ" : "UNREAD";
						String subjectDisplay = mail.getSubject() + (mail.hasAttachments() ? " 📎" : "");
						tableModel.addRow(new Object[] { status, mail.getSender(), subjectDisplay });
					}
					txtViewer.setText("Inbox updated and synced.\n" + currentEmailList.size() + " messages.");
					btnRefresh.setEnabled(true);
				} catch (Exception ex) {
					txtViewer.setText("Connection Error.");
					JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
					btnRefresh.setEnabled(true);
				}
			}).start();
		});

		emailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = emailTable.getSelectedRow();
				if (row >= 0 && row < currentEmailList.size()) {
					EmailModel mail = currentEmailList.get(row);

					String attachmentsInfo = "";
					if (mail.hasAttachments()) {
						attachmentsInfo = "\n\n=== ATTACHMENTS ===\n";
						for (String n : mail.getAttachmentNames())
							attachmentsInfo += "- " + n + "\n";
					}
					btnDownloadEmail.setEnabled(true);
					txtViewer.setText("FROM: " + mail.getSender() + "\nSUBJECT: " + mail.getSubject()
							+ "\n----------------\n" + mail.getContent() + attachmentsInfo);
				}
			}
		});

		btnDownloadEmail.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			EmailModel mail = currentEmailList.get(row);
			JFileChooser fileChooser = new JFileChooser();
			String safeSubject = mail.getSubject().replaceAll("[^a-zA-Z0-9.-]", "_");
			if (safeSubject.length() > 20)
				safeSubject = safeSubject.substring(0, 20);
			fileChooser.setSelectedFile(new File(safeSubject + ".eml"));

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File dest = fileChooser.getSelectedFile();
				if (!dest.getName().toLowerCase().endsWith(".eml"))
					dest = new File(dest.getAbsolutePath() + ".eml");
				final File finalDest = dest;
				new Thread(() -> {
					try {
						backend.downloadEmailComplete(mail.getUniqueId(), finalDest);
					} catch (Exception ex) {
					}
				}).start();
			}
		});

		btnToggleRead.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			EmailModel mail = currentEmailList.get(row);
			boolean newStatus = !mail.isRead();
			mail.setRead(newStatus);
			tableModel.setValueAt(newStatus ? "READ" : "UNREAD", row, 0);
			new Thread(() -> {
				try {
					backend.updateReadStatusIMAP(mail.getSubject(), mail.getSender(), mail.getUniqueId(), newStatus);
				} catch (Exception ex) {
				}
			}).start();
		});

		btnDelete.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
			EmailModel mail = currentEmailList.get(row);
			new Thread(() -> {
				try {
					backend.deleteEmail(mail.getSubject(), mail.getSender());
					currentEmailList.remove(row);
					tableModel.removeRow(row);
					txtViewer.setText("Deleted.");
				} catch (Exception ex) {
				}
			}).start();
		});
	}
}
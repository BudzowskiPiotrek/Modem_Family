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

	// Redactar
	private JButton btnSend, btnAttach, btnClearAttach;
	private JLabel lblAttachedFile;
	private List<File> attachmentsList = new ArrayList<>();

	// Inbox
	private JButton btnRefresh, btnDelete, btnToggleRead;
	// BOTÓN DE DESCARGA
	private JButton btnDownloadEmail; // Ahora descarga el .EML completo

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

		// --- TOP: REDACTAR ---
		JPanel composePanel = new JPanel(new BorderLayout(5, 5));
		composePanel.setBorder(new TitledBorder("Redactar"));

		JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
		fieldsPanel.add(new JLabel("Para:"));
		txtTo = new JTextField();
		fieldsPanel.add(txtTo);
		fieldsPanel.add(new JLabel("Asunto:"));
		txtSubject = new JTextField();
		fieldsPanel.add(txtSubject);

		txtBody = new JTextArea(3, 20);

		JPanel bottomComposePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnAttach = new JButton("Adjuntar (+)");
		btnClearAttach = new JButton("X");
		btnClearAttach.setForeground(Color.RED);
		lblAttachedFile = new JLabel("0 archivos");
		lblAttachedFile.setForeground(Color.GRAY);
		btnSend = new JButton("Enviar Email");

		bottomComposePanel.add(btnAttach);
		bottomComposePanel.add(btnClearAttach);
		bottomComposePanel.add(lblAttachedFile);
		bottomComposePanel.add(Box.createHorizontalStrut(30));
		bottomComposePanel.add(btnSend);

		composePanel.add(fieldsPanel, BorderLayout.NORTH);
		composePanel.add(new JScrollPane(txtBody), BorderLayout.CENTER);
		composePanel.add(bottomComposePanel, BorderLayout.SOUTH);

		// --- BOTTOM: INBOX ---
		JPanel inboxPanel = new JPanel(new BorderLayout(5, 5));
		inboxPanel.setBorder(new TitledBorder("Bandeja de Entrada"));

		String[] columns = { "Estado", "Remitente", "Asunto" };
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		emailTable = new JTable(tableModel);
		emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		txtViewer = new JTextArea("Selecciona un correo...");
		txtViewer.setLineWrap(true);
		txtViewer.setEditable(false);
		txtViewer.setBackground(new Color(245, 245, 245));

		JSplitPane splitInbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(emailTable),
				new JScrollPane(txtViewer));
		splitInbox.setDividerLocation(500);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		btnRefresh = new JButton("Actualizar");
		btnToggleRead = new JButton("Marcar Leído/No Leído");

		// BOTÓN DESCARGAR COMPLETO
		btnDownloadEmail = new JButton("Descargar Email (.eml)");
		btnDownloadEmail.setEnabled(false);

		btnDelete = new JButton("Eliminar");
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

		// --- ADJUNTAR Y ENVIAR ---
		btnAttach.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				attachmentsList.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
				lblAttachedFile.setText(attachmentsList.size() + " archivos listos");
				lblAttachedFile.setForeground(new Color(0, 100, 0));
			}
		});

		btnClearAttach.addActionListener(e -> {
			attachmentsList.clear();
			lblAttachedFile.setText("0 archivos");
			lblAttachedFile.setForeground(Color.GRAY);
		});

		btnSend.addActionListener(e -> {
			new Thread(() -> {
				try {
					btnSend.setEnabled(false);
					btnSend.setText("Enviando...");
					backend.sendEmail(txtTo.getText(), txtSubject.getText(), txtBody.getText(), attachmentsList);
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(this, "Enviado.");
						txtTo.setText("");
						txtSubject.setText("");
						txtBody.setText("");
						attachmentsList.clear();
						lblAttachedFile.setText("0 archivos");
						btnSend.setText("Enviar Email");
						btnSend.setEnabled(true);
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
						btnSend.setEnabled(true);
					});
				}
			}).start();
		});

		// --- INBOX ---
		btnRefresh.addActionListener(e -> {
			new Thread(() -> {
				SwingUtilities.invokeLater(() -> {
					btnRefresh.setEnabled(false);
					tableModel.setRowCount(0);
					txtViewer.setText("Cargando...");
				});
				try {
					currentEmailList = backend.fetchEmails();
					SwingUtilities.invokeLater(() -> {
						for (EmailModel mail : currentEmailList) {
							String status = mail.isRead() ? "LEÍDO" : "NO LEÍDO";
							// Icono clip si tiene adjuntos
							String subjectDisplay = mail.getSubject() + (mail.hasAttachments() ? " 📎" : "");
							tableModel.addRow(new Object[] { status, mail.getSender(), subjectDisplay });
						}
						txtViewer.setText("Actualizado.");
						btnRefresh.setEnabled(true);
					});
				} catch (Exception ex) {
					SwingUtilities.invokeLater(() -> {
						txtViewer.setText("Error.");
						btnRefresh.setEnabled(true);
					});
				}
			}).start();
		});

		// --- SELECCIONAR CORREO (Habilita botón descargar) ---
		emailTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = emailTable.getSelectedRow();
				if (row >= 0 && row < currentEmailList.size()) {
					EmailModel mail = currentEmailList.get(row);

					String infoAdjuntos = "";
					if (mail.hasAttachments()) {
						infoAdjuntos = "\n\n[ESTE CORREO TIENE " + mail.getAttachmentNames().size()
								+ " ARCHIVOS ADJUNTOS]\n";
						for (String n : mail.getAttachmentNames())
							infoAdjuntos += "- " + n + "\n";
					}

					// Siempre permitimos descargar el correo completo (.eml), tenga adjuntos o no
					btnDownloadEmail.setEnabled(true);

					txtViewer.setText("DE: " + mail.getSender() + "\nASUNTO: " + mail.getSubject()
							+ "\n----------------\n" + mail.getContent() + infoAdjuntos);
				}
			}
		});

		// --- BOTÓN DESCARGAR EMAIL COMPLETO ---
		btnDownloadEmail.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			EmailModel mail = currentEmailList.get(row);

			JFileChooser fileChooser = new JFileChooser();
			// Sugerimos nombre seguro para el archivo
			String safeSubject = mail.getSubject().replaceAll("[^a-zA-Z0-9.-]", "_");
			if (safeSubject.length() > 20)
				safeSubject = safeSubject.substring(0, 20);
			fileChooser.setSelectedFile(new File(safeSubject + ".eml"));

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File dest = fileChooser.getSelectedFile();
				// Asegurar extensión .eml
				if (!dest.getName().toLowerCase().endsWith(".eml")) {
					dest = new File(dest.getAbsolutePath() + ".eml");
				}

				final File finalDest = dest;
				new Thread(() -> {
					try {
						backend.downloadEmailComplete(mail.getUniqueId(), finalDest);
						SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
								"Email guardado en: " + finalDest.getAbsolutePath()));
					} catch (Exception ex) {
						SwingUtilities.invokeLater(
								() -> JOptionPane.showMessageDialog(this, "Error descargando: " + ex.getMessage()));
					}
				}).start();
			}
		});

		// (Resto de botones ToggleRead y Delete igual)
		btnToggleRead.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			EmailModel mail = currentEmailList.get(row);
			boolean nuevoEstado = !mail.isRead();
			mail.setRead(nuevoEstado);
			tableModel.setValueAt(nuevoEstado ? "LEÍDO" : "NO LEÍDO", row, 0);
			new Thread(() -> {
				try {
					backend.updateReadStatusIMAP(mail.getSubject(), mail.getSender(), mail.getUniqueId(), nuevoEstado);
				} catch (Exception ex) {
				}
			}).start();
		});

		btnDelete.addActionListener(e -> {
			int row = emailTable.getSelectedRow();
			if (row == -1)
				return;
			if (JOptionPane.showConfirmDialog(this, "¿Borrar?", "Confirmar",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
			EmailModel mail = currentEmailList.get(row);
			new Thread(() -> {
				try {
					backend.deleteEmail(mail.getSubject(), mail.getSender());
					SwingUtilities.invokeLater(() -> {
						currentEmailList.remove(row);
						tableModel.removeRow(row);
						txtViewer.setText("Eliminado.");
					});
				} catch (Exception ex) {
				}
			}).start();
		});
	}
}
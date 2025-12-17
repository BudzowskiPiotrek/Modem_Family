package metroMalaga.Controller.smtp;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import metroMalaga.Controller.smtp.tasks.*;
import metroMalaga.Model.EmailModel;

public class ButtonHandleSMTP extends MouseAdapter implements ActionListener {

	private final Component parentView;
	private final HandleSMTP backend;

	private final JTextField txtTo;
	private final JTextField txtSubject;
	private final JTextArea txtBody;
	private final JLabel lblAttachedFile;

	private final JButton btnSend;
	private final JButton btnAttach;
	private final JButton btnClearAttach;
	private final JButton btnRefresh;
	private final JButton btnToggleRead;
	private final JButton btnDownloadEmail;
	private final JButton btnDelete;

	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;

	private List<EmailModel> currentEmailList;
	private List<File> attachmentsList;

	public ButtonHandleSMTP(Component parentView, HandleSMTP backend, JTextField txtTo, JTextField txtSubject,
			JTextArea txtBody, JLabel lblAttachedFile, JButton btnSend, JButton btnAttach, JButton btnClearAttach,
			JButton btnRefresh, JButton btnToggleRead, JButton btnDownloadEmail, JButton btnDelete, JTable emailTable,
			DefaultTableModel tableModel, JTextArea txtViewer) {
		this.parentView = parentView;
		this.backend = backend;
		this.txtTo = txtTo;
		this.txtSubject = txtSubject;
		this.txtBody = txtBody;
		this.lblAttachedFile = lblAttachedFile;
		this.btnSend = btnSend;
		this.btnAttach = btnAttach;
		this.btnClearAttach = btnClearAttach;
		this.btnRefresh = btnRefresh;
		this.btnToggleRead = btnToggleRead;
		this.btnDownloadEmail = btnDownloadEmail;
		this.btnDelete = btnDelete;
		this.emailTable = emailTable;
		this.tableModel = tableModel;
		this.txtViewer = txtViewer;

		this.currentEmailList = new ArrayList<>();
		this.attachmentsList = new ArrayList<>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == btnAttach) {
			attachFiles();
		} else if (source == btnClearAttach) {
			clearAttachments();
		} else if (source == btnSend) {
			sendEmail();
		} else if (source == btnRefresh) {
			refreshInbox();
		} else if (source == btnToggleRead) {
			toggleReadStatus();
		} else if (source == btnDownloadEmail) {
			downloadFullEmail();
		} else if (source == btnDelete) {
			deleteEmail();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == emailTable) {
			int row = emailTable.getSelectedRow();
			if (row >= 0 && row < currentEmailList.size()) {
				EmailModel mail = currentEmailList.get(row);

				String attachmentsInfo = "";
				if (mail.hasAttachments()) {
					attachmentsInfo = "\n\n=== ATTACHED OBJECTS ===\n";
					for (String n : mail.getAttachmentNames())
						attachmentsInfo += "> " + n + "\n";
				}

				btnDownloadEmail.setEnabled(true);
				txtViewer.setText("SOURCE: " + mail.getSender() + "\nTOPIC: " + mail.getSubject()
						+ "\n--------------------------------\n" + mail.getContent() + attachmentsInfo);
			}
		}
	}

	private void attachFiles() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		if (fileChooser.showOpenDialog(parentView) == JFileChooser.APPROVE_OPTION) {
			attachmentsList.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
			updateAttachmentLabel();
		}
	}

	private void clearAttachments() {
		attachmentsList.clear();
		lblAttachedFile.setText("NO FILES");
		lblAttachedFile.setForeground(new Color(150, 150, 150));
		lblAttachedFile.setToolTipText(null);
	}

	private void updateAttachmentLabel() {
		StringBuilder sb = new StringBuilder();
		for (File f : attachmentsList) {
			sb.append(f.getName()).append(", ");
		}
		String text = sb.toString();
		if (text.length() > 2)
			text = text.substring(0, text.length() - 2);
		if (text.length() > 30)
			text = text.substring(0, 30) + "...";

		lblAttachedFile.setText(text);
		lblAttachedFile.setForeground(Color.RED);
		lblAttachedFile.setToolTipText(sb.toString());
	}

	private void sendEmail() {
		String recipient = txtTo.getText().trim();
		String subject = txtSubject.getText().trim();
		String body = txtBody.getText().trim();

		if (recipient.isEmpty()) {
			JOptionPane.showMessageDialog(parentView, "The 'To' field is required.", "MISSING DATA",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		EmailSenderTask task = new EmailSenderTask(backend, recipient, subject, body, attachmentsList, parentView,
				btnSend, txtTo, txtSubject, txtBody, lblAttachedFile, attachmentsList);
		new Thread(task).start();
	}

	private void refreshInbox() {
		RefreshInboxTask task = new RefreshInboxTask(backend, btnRefresh, tableModel, txtViewer, currentEmailList);
		new Thread(task).start();
	}

	private void toggleReadStatus() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;

		EmailModel mail = currentEmailList.get(row);
		boolean newStatus = !mail.isRead();
		mail.setRead(newStatus);
		tableModel.setValueAt(newStatus ? "SEEN" : "NEW", row, 0);

		ToggleReadStatusTask task = new ToggleReadStatusTask(backend, mail.getSubject(), mail.getSender(),
				mail.getUniqueId(), newStatus);
		new Thread(task).start();
	}

	private void downloadFullEmail() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;

		EmailModel mail = currentEmailList.get(row);
		JFileChooser fileChooser = new JFileChooser();
		String safeSubject = mail.getSubject().replaceAll("[^a-zA-Z0-9.-]", "_");
		if (safeSubject.length() > 20)
			safeSubject = safeSubject.substring(0, 20);
		fileChooser.setSelectedFile(new File(safeSubject + ".eml"));

		if (fileChooser.showSaveDialog(parentView) == JFileChooser.APPROVE_OPTION) {
			File dest = fileChooser.getSelectedFile();
			if (!dest.getName().toLowerCase().endsWith(".eml"))
				dest = new File(dest.getAbsolutePath() + ".eml");

			DownloadEmailTask task = new DownloadEmailTask(backend, mail.getUniqueId(), dest, parentView);
			new Thread(task).start();
		}
	}

	private void deleteEmail() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;
		if (JOptionPane.showConfirmDialog(parentView, "Eliminate this email?", "CONFIRM",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;

		EmailModel mail = currentEmailList.get(row);

		DeleteEmailTask task = new DeleteEmailTask(backend, mail.getSubject(), mail.getSender(), row, currentEmailList,
				tableModel, txtViewer);
		new Thread(task).start();
	}

}
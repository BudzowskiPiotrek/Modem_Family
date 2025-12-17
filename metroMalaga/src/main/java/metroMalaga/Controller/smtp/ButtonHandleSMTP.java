package metroMalaga.Controller.smtp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import metroMalaga.Controller.smtp.tasks.*;
import metroMalaga.Model.EmailModel;
import metroMalaga.View.PanelSMTP;

public class ButtonHandleSMTP implements ActionListener {

	private final PanelSMTP parentView;
	private final HandleSMTP backend;

	private final JTextField txtTo;
	private final JTextField txtSubject;
	private final JTextArea txtBody;
	private final JLabel lblAttachedFile;

	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;

	private List<EmailModel> currentEmailList;
	private List<File> attachmentsList;

	public ButtonHandleSMTP(PanelSMTP parentView, HandleSMTP backend, JTextField txtTo, JTextField txtSubject,
			JTextArea txtBody, JLabel lblAttachedFile, JTable emailTable, DefaultTableModel tableModel,
			JTextArea txtViewer) {
		this.parentView = parentView;
		this.backend = backend;
		this.txtTo = txtTo;
		this.txtSubject = txtSubject;
		this.txtBody = txtBody;
		this.lblAttachedFile = lblAttachedFile;
		this.emailTable = emailTable;
		this.tableModel = tableModel;
		this.txtViewer = txtViewer;

		this.currentEmailList = new ArrayList<>();
		this.attachmentsList = new ArrayList<>();

		registerListeners();
	}

	private void registerListeners() {
		for (JButton btn : Arrays.asList(parentView.getBtnAttach(), parentView.getBtnClearAttach(),
				parentView.getBtnSend(), parentView.getBtnRefresh(), parentView.getBtnToggleRead(),
				parentView.getBtnDownloadEmail(), parentView.getBtnDelete())) {
			btn.addActionListener(this);
		}

		MouseClickEmail mouseListener = new MouseClickEmail(parentView.getEmailTable(), txtViewer,
				parentView.getBtnDownloadEmail(), currentEmailList);
		parentView.getEmailTable().addMouseListener(mouseListener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == parentView.getBtnAttach()) {
			attachFiles();
		} else if (source == parentView.getBtnClearAttach()) {
			clearAttachments();
		} else if (source == parentView.getBtnSend()) {
			sendEmail();
		} else if (source == parentView.getBtnRefresh()) {
			refreshInbox();
		} else if (source == parentView.getBtnToggleRead()) {
			toggleReadStatus();
		} else if (source == parentView.getBtnDownloadEmail()) {
			downloadFullEmail();
		} else if (source == parentView.getBtnDelete()) {
			deleteEmail();
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
				parentView.getBtnSend(), txtTo, txtSubject, txtBody, lblAttachedFile, attachmentsList);
		new Thread(task).start();
	}

	private void refreshInbox() {
		RefreshInboxTask task = new RefreshInboxTask(backend, parentView.getBtnRefresh(), tableModel, txtViewer,
				currentEmailList);
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

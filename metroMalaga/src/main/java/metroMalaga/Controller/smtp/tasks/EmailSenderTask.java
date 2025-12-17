package metroMalaga.Controller.smtp.tasks;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.List;
import javax.swing.*;
import metroMalaga.Controller.smtp.HandleSMTP;

public class EmailSenderTask implements Runnable {

	private final HandleSMTP backend;
	private final Component parentView;
	private final String recipient, subject, body;
	private final List<File> attachments;
	private final JButton btnSend;
	private final JTextField txtTo, txtSubject;
	private final JTextArea txtBody;
	private final JLabel lblAttachedFile;

	public EmailSenderTask(HandleSMTP backend, Component parentView, String recipient, String subject, String body,
			List<File> attachments, JButton btnSend, JTextField txtTo, JTextField txtSubject, JTextArea txtBody,
			JLabel lblAttachedFile) {
		this.backend = backend;
		this.parentView = parentView;
		this.recipient = recipient;
		this.subject = subject;
		this.body = body;
		this.attachments = attachments;
		this.btnSend = btnSend;
		this.txtTo = txtTo;
		this.txtSubject = txtSubject;
		this.txtBody = txtBody;
		this.lblAttachedFile = lblAttachedFile;
	}

	@Override
	public void run() {
		btnSend.setEnabled(false);
		btnSend.setText("SENDING...");

		try {
			backend.sendEmail(recipient, subject, body, attachments);
			JOptionPane.showMessageDialog(parentView, "Email sent.");
			txtTo.setText("");
			txtSubject.setText("");
			txtBody.setText("");
			attachments.clear();
			lblAttachedFile.setText("NO FILES");
			lblAttachedFile.setForeground(Color.GRAY);
			btnSend.setText("SEND EMAIL");
			btnSend.setEnabled(true);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(parentView, "Error: " + ex.getMessage());
			btnSend.setText("SEND EMAIL");
			btnSend.setEnabled(true);
		}
	}
}
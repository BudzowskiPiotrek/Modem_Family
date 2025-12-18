package metroMalaga.Controller.smtp.tasks;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.List;
import javax.swing.*;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.Language;

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
		btnSend.setText(Language.get(165));

		try {
			backend.sendEmail(recipient, subject, body, attachments);
			JOptionPane.showMessageDialog(parentView, Language.get(166));
			txtTo.setText("");
			txtSubject.setText("");
			txtBody.setText("");
			attachments.clear();
			lblAttachedFile.setText(Language.get(148));
			lblAttachedFile.setForeground(Color.GRAY);
			btnSend.setText(Language.get(59));
			btnSend.setEnabled(true);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(parentView, Language.get(164) + ex.getMessage());
			btnSend.setText(Language.get(59));
			btnSend.setEnabled(true);
		}
	}
}

package metroMalaga.Controller.smtp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import metroMalaga.Controller.ServiceSMTP;
import metroMalaga.Controller.smtp.tasks.*;
import metroMalaga.Model.EmailModel;
import metroMalaga.Model.Language;
import metroMalaga.View.PanelSMTP;

public class ButtonHandleSMTP implements ActionListener {

	private final PanelSMTP view;
	private final HandleSMTP backend;
	private ServiceSMTP serviceSMTP;
	private Runnable onReturnCallback;

	private final JTextField txtTo;
	private final JTextField txtSubject;
	private final JTextArea txtBody;
	private final JLabel lblAttachedFile;
	private final JButton btnSend, btnAttach, btnClearAttach, btnRefresh, btnToggleRead, btnDownloadEmail, btnDelete,
			btnReturn;
	private final JTable emailTable;
	private final DefaultTableModel tableModel;
	private final JTextArea txtViewer;

	private List<EmailModel> currentEmailList;
	private List<File> attachmentsList;

	private final Set<String> pendingUpdateIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

	public ButtonHandleSMTP(PanelSMTP view, HandleSMTP backend) {
		this.view = view;
		this.backend = backend;
		this.serviceSMTP = new ServiceSMTP();

		this.txtTo = view.getTxtTo();
		this.txtSubject = view.getTxtSubject();
		this.txtBody = view.getTxtBody();
		this.lblAttachedFile = view.getLblAttachedFile();
		this.btnSend = view.getBtnSend();
		this.btnAttach = view.getBtnAttach();
		this.btnClearAttach = view.getBtnClearAttach();
		this.btnRefresh = view.getBtnRefresh();
		this.btnToggleRead = view.getBtnToggleRead();
		this.btnDownloadEmail = view.getBtnDownloadEmail();
		this.btnDelete = view.getBtnDelete();
		this.btnReturn = view.getBtnReturn();
		this.emailTable = view.getEmailTable();
		this.tableModel = view.getTableModel();
		this.txtViewer = view.getTxtViewer();

		this.currentEmailList = new ArrayList<>();
		this.attachmentsList = new ArrayList<>();

		initListeners();

		new Thread(new AutoRefreshAgent(this)).start();
	}

	public void addPendingId(String uid) {
		if (uid != null) pendingUpdateIds.add(uid);
	}

	public void removePendingId(String uid) {
		if (uid != null) pendingUpdateIds.remove(uid);
	}

	public boolean isPending(String uid) {
		return uid != null && pendingUpdateIds.contains(uid);
	}

	private void initListeners() {
		btnAttach.addActionListener(this);
		btnClearAttach.addActionListener(this);
		btnSend.addActionListener(this);
		btnRefresh.addActionListener(this);
		btnToggleRead.addActionListener(this);
		btnDownloadEmail.addActionListener(this);
		btnDelete.addActionListener(this);
		if (btnReturn != null)
			btnReturn.addActionListener(this);

		MouseClickListener mouseListener = new MouseClickListener(
				backend,
				this,
				emailTable,
				tableModel,
				txtViewer,
				btnDownloadEmail);
		emailTable.addMouseListener(mouseListener);
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
			refreshInbox(false);
		} else if (source == btnToggleRead) {
			toggleReadStatus();
		} else if (source == btnDownloadEmail) {
			downloadFullEmail();
		} else if (source == btnDelete) {
			deleteEmail();
		} else if (source == btnReturn) {
			if (onReturnCallback != null) {
				onReturnCallback.run();
			}
		}
	}

	public void setOnReturnCallback(Runnable callback) {
		this.onReturnCallback = callback;
	}

	public void displayContent(EmailModel mail) {
		String attachmentsInfo = "";
		if (mail.hasAttachments()) {
			attachmentsInfo = Language.get(159);
			for (String n : mail.getAttachmentNames())
				attachmentsInfo += "> " + n + "\n";
		}
		btnDownloadEmail.setEnabled(true);
		txtViewer.setText(Language.get(157) + mail.getSender() + 
				"\n" + Language.get(158) + mail.getSubject() +
				"\n--------------------------------\n" + mail.getContent() + attachmentsInfo);
	}

	private void attachFiles() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			attachmentsList.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
			updateAttachmentLabel();
		}
	}

	private void clearAttachments() {
		attachmentsList.clear();
		lblAttachedFile.setText(Language.get(148));
		lblAttachedFile.setForeground(Color.GRAY);
	}

	private void updateAttachmentLabel() {
		StringBuilder sb = new StringBuilder();
		for (File f : attachmentsList)
			sb.append(f.getName()).append(", ");
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
			JOptionPane.showMessageDialog(view, Language.get(149));
			return;
		}

		if (!serviceSMTP.isEmailInWhitelist(recipient) && !serviceSMTP.isEmailInUsers(recipient)) {
			JOptionPane.showMessageDialog(view,
					Language.get(150) + recipient + Language.get(151),
					Language.get(152),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		EmailSenderTask task = new EmailSenderTask(backend, view, recipient, subject, body, attachmentsList,
				btnSend, txtTo, txtSubject, txtBody, lblAttachedFile);
		new Thread(task).start();
	}

	public void refreshInbox(boolean isAuto) {
		RefreshInboxTask task = new RefreshInboxTask(backend, isAuto, btnRefresh, txtViewer, emailTable, tableModel,
				currentEmailList, this);
		new Thread(task).start();
	}

	private void toggleReadStatus() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;
		EmailModel mail = currentEmailList.get(row);
		boolean newSt = !mail.isRead();
		
		addPendingId(mail.getUniqueId());
		
		mail.setRead(newSt);
		tableModel.setValueAt(newSt ? Language.get(153) : Language.get(154), row, 0);

		new Thread(() -> {
			try {
				backend.updateReadStatusIMAP(mail.getUniqueId(), newSt);
				Thread.sleep(2000); 
			} catch (Exception e) {
			} finally {
				removePendingId(mail.getUniqueId());
			}
		}).start();
	}

	private void downloadFullEmail() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;
		EmailModel mail = currentEmailList.get(row);
		JFileChooser fc = new JFileChooser();
		String safe = mail.getSubject().replaceAll("[^a-zA-Z0-9.-]", "_");
		if (safe.length() > 20)
			safe = safe.substring(0, 20);
		fc.setSelectedFile(new File(safe + ".eml"));
		if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
			File dest = fc.getSelectedFile();
			if (!dest.getName().endsWith(".eml"))
				dest = new File(dest.getAbsolutePath() + ".eml");

			DownloadEmailTask task = new DownloadEmailTask(backend, view, mail.getUniqueId(), dest);
			new Thread(task).start();
		}
	}

	private void deleteEmail() {
		int row = emailTable.getSelectedRow();
		if (row == -1)
			return;
		if (JOptionPane.showConfirmDialog(view, Language.get(155), Language.get(156),
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		EmailModel mail = currentEmailList.get(row);

		DeleteEmailTask task = new DeleteEmailTask(backend, txtViewer, currentEmailList, tableModel, mail.getUniqueId(),
				row);
		new Thread(task).start();
	}

	public List<EmailModel> getCurrentEmailList() {
		return currentEmailList;
	}
}

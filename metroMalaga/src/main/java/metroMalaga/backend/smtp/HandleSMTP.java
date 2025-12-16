package metroMalaga.backend.smtp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.search.FlagTerm;
import metroMalaga.Clases.EmailModel;

import java.io.*;
import java.util.*;

public class HandleSMTP {

	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final String POP3_HOST = "pop.gmail.com";
	private static final String IMAP_HOST = "imap.gmail.com";

	private String userEmail;
	private String appPassword;

	private final File readEmailsFile = new File("read_emails_db.dat");
	private Set<String> locallyRead = new HashSet<>();

	public boolean login(String email, String password) {
		this.userEmail = email;
		this.appPassword = password;
		loadReadFromDisk();
		return true;
	}

	private void loadReadFromDisk() {
		if (!readEmailsFile.exists())
			return;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(readEmailsFile))) {
			locallyRead = (Set<String>) ois.readObject();
		} catch (Exception e) {
			locallyRead = new HashSet<>();
		}
	}

	private void saveReadToDisk() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(readEmailsFile))) {
			oos.writeObject(locallyRead);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --- SEND EMAIL ---
	public void sendEmail(String recipient, String subject, String body, List<File> attachments) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userEmail, appPassword);
			}
		});

		Message message = new MimeMessage(session);
		// Basic backend validations
		if (recipient == null || recipient.isEmpty())
			throw new Exception("The recipient cannot be empty.");

		message.setFrom(new InternetAddress(userEmail));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		message.setSubject(subject);

		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(body);
		multipart.addBodyPart(messageBodyPart);

		if (attachments != null && !attachments.isEmpty()) {
			for (File file : attachments) {
				if (file.exists()) {
					MimeBodyPart attachPart = new MimeBodyPart();
					attachPart.attachFile(file);
					multipart.addBodyPart(attachPart);
				}
			}
		}
		message.setContent(multipart);
		Transport.send(message);
	}

	// --- FETCH EMAILS (POP3 + AUTOMATIC IMAP SYNC) ---
	public List<EmailModel> fetchEmails() {
		List<EmailModel> emailList = new ArrayList<>();

		// 1. POP3 PHASE: BASIC DOWNLOAD
		try {
			Properties props = new Properties();
			props.put("mail.pop3.host", POP3_HOST);
			props.put("mail.pop3.port", "995");
			props.put("mail.pop3.starttls.enable", "true");
			props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("pop3");
			store.connect(POP3_HOST, "recent:" + userEmail, appPassword);

			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			int totalMessages = emailFolder.getMessageCount();
			int start = Math.max(1, totalMessages - 100);

			if (totalMessages > 0) {
				Message[] messages = emailFolder.getMessages(start, totalMessages);
				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				emailFolder.fetch(messages, fp);

				for (int i = messages.length - 1; i >= 0; i--) {
					Message msg = messages[i];

					Address[] fromAddresses = msg.getFrom();
					String sender = (fromAddresses != null && fromAddresses.length > 0) ? fromAddresses[0].toString()
							: "Unknown";

					// FILTER: Skip my own sent emails
					if (sender.contains(userEmail))
						continue;

					String uid = "";
					if (msg instanceof MimeMessage)
						uid = ((MimeMessage) msg).getMessageID();

					String bodyText = "";
					List<String> attachments = new ArrayList<>();
					try {
						Object content = msg.getContent();
						if (content instanceof Multipart) {
							parseMultipart((Multipart) content, attachments);
							bodyText = getTextFromMultipart((Multipart) content);
						} else if (content instanceof String) {
							bodyText = (String) content;
						}
					} catch (Exception e) {
						bodyText = "[Complex Content]";
					}

					EmailModel email = new EmailModel(msg.getMessageNumber(), sender, msg.getSubject(), bodyText);
					email.setUniqueId(uid);
					email.setAttachmentNames(attachments);

					// Initial status based on local memory
					email.setRead(locallyRead.contains(uid));

					emailList.add(email);
				}
			}
			emailFolder.close(false);
			store.close();

			if (!emailList.isEmpty()) {
				syncReadStatusWithGmail(emailList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return emailList;
	}

	/**
	 * MAGIC METHOD: Connects via IMAP briefly, gets the IDs of read emails, and
	 * updates our POP3 list and local database.
	 */
	private void syncReadStatusWithGmail(List<EmailModel> currentList) {
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");
			props.put("mail.imaps.host", IMAP_HOST);
			props.put("mail.imaps.port", "993");

			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imaps");
			store.connect(IMAP_HOST, userEmail, appPassword);

			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// Search ONLY for messages that have the SEEN flag (Read)
			// This is much more efficient than downloading everything
			Message[] seenMessages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE); // We need the Message-ID
			emailFolder.fetch(seenMessages, fp);

			// Create a quick set with read IDs from the server
			Set<String> serverReadIds = new HashSet<>();
			for (Message msg : seenMessages) {
				if (msg instanceof MimeMessage) {
					serverReadIds.add(((MimeMessage) msg).getMessageID());
				}
			}

			// Update our local list and save
			boolean saveChanges = false;
			for (EmailModel localMail : currentList) {
				if (serverReadIds.contains(localMail.getUniqueId())) {
					localMail.setRead(true);
					if (!locallyRead.contains(localMail.getUniqueId())) {
						locallyRead.add(localMail.getUniqueId());
						saveChanges = true;
					}
				}
			}

			if (saveChanges)
				saveReadToDisk();

			emailFolder.close(false);
			store.close();

		} catch (Exception e) {
			System.out.println("Could not sync status with Gmail (IMAP): " + e.getMessage());
			// Do not throw fatal error, simply keep the list as it was in POP3
		}
	}

	// --- PARSERS AND DOWNLOAD (Same as before) ---
	private void parseMultipart(Multipart multipart, List<String> attachments) {
		try {
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
						|| (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
					attachments.add(bodyPart.getFileName());
				} else if (bodyPart.getContent() instanceof Multipart) {
					parseMultipart((Multipart) bodyPart.getContent(), attachments);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getTextFromMultipart(Multipart multipart) {
		StringBuilder result = new StringBuilder();
		try {
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (bodyPart.isMimeType("text/plain")) {
					result.append(bodyPart.getContent());
				} else if (bodyPart.getContent() instanceof Multipart) {
					result.append(getTextFromMultipart((Multipart) bodyPart.getContent()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public void downloadEmailComplete(String uid, File destinationFile) {
		try {
			Properties props = new Properties();
			props.put("mail.pop3.host", POP3_HOST);
			props.put("mail.pop3.port", "995");
			props.put("mail.pop3.starttls.enable", "true");
			props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("pop3");
			store.connect(POP3_HOST, "recent:" + userEmail, appPassword);
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			Message[] messages = emailFolder.getMessages();
			Message targetMsg = null;
			for (int i = messages.length - 1; i >= 0; i--) {
				if (messages[i] instanceof MimeMessage) {
					if (((MimeMessage) messages[i]).getMessageID().equals(uid)) {
						targetMsg = messages[i];
						break;
					}
				}
			}
			if (targetMsg == null)
				throw new Exception("Message not found.");
			try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
				targetMsg.writeTo(fos);
			}
			emailFolder.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteEmail(String subjectToDelete, String senderToDelete) {
		try {
			Properties props = new Properties();
			props.put("mail.pop3.host", POP3_HOST);
			props.put("mail.pop3.port", "995");
			props.put("mail.pop3.starttls.enable", "true");
			props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("pop3");
			store.connect(POP3_HOST, "recent:" + userEmail, appPassword);
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);
			Message[] messages = emailFolder.getMessages();
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			emailFolder.fetch(messages, fp);
			boolean found = false;
			for (Message msg : messages) {
				try {
					String currentSubject = (msg.getSubject() == null) ? "" : msg.getSubject();
					Address[] addr = msg.getFrom();
					String currentSender = (addr != null && addr.length > 0) ? addr[0].toString() : "";
					if (currentSubject.trim().equalsIgnoreCase(subjectToDelete.trim())
							&& currentSender.contains(senderToDelete)) {
						msg.setFlag(Flags.Flag.DELETED, true);
						found = true;
						break;
					}
				} catch (Exception e) {
				}
			}
			if (found)
				emailFolder.close(true);
			else
				emailFolder.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateReadStatusIMAP(String subject, String sender, String uid, boolean markAsRead) {
		if (markAsRead) {
			if (uid != null)
				locallyRead.add(uid);
		} else {
			if (uid != null)
				locallyRead.remove(uid);
		}
		saveReadToDisk();
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");
			props.put("mail.imaps.host", IMAP_HOST);
			props.put("mail.imaps.port", "993");
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imaps");
			store.connect(IMAP_HOST, userEmail, appPassword);
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);
			Message[] messages = emailFolder.getMessages();
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			emailFolder.fetch(messages, fp);
			for (Message msg : messages) {
				try {
					String currentUid = (msg instanceof MimeMessage) ? ((MimeMessage) msg).getMessageID() : "";
					if (uid != null && uid.equals(currentUid)) {
						msg.setFlag(Flags.Flag.SEEN, markAsRead);
						break;
					}
				} catch (Exception e) {
				}
			}
			emailFolder.close(true);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
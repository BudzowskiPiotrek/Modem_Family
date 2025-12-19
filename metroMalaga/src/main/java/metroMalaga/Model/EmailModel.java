package metroMalaga.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing an email message.
 */
public class EmailModel {
	private int messageNumber;
	private String sender;
	private String subject;
	private String content;
	private boolean isRead;
	private String uniqueId;
	private List<String> attachmentNames;

	/**
	 * Constructor for EmailModel.
	 * 
	 * @param messageNumber The sequence number of the message.
	 * @param sender        The sender's email address.
	 * @param subject       The subject of the email.
	 * @param content       The content/body of the email.
	 */
	public EmailModel(int messageNumber, String sender, String subject, String content) {
		this.messageNumber = messageNumber;
		this.sender = sender;
		this.subject = subject;
		this.content = content;
		this.isRead = false;
		this.uniqueId = "";
		this.attachmentNames = new ArrayList<>();
	}

	// Getters and Setters

	/**
	 * Gets the message number.
	 * 
	 * @return The message number.
	 */
	public int getMessageNumber() {
		return messageNumber;
	}

	/**
	 * Gets the sender's email address.
	 * 
	 * @return The sender's email.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Gets the subject of the email.
	 * 
	 * @return The subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Gets the content of the email.
	 * 
	 * @return The content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Checks if the email has been read.
	 * 
	 * @return true if read, false otherwise.
	 */
	public boolean isRead() {
		return isRead;
	}

	/**
	 * Sets the read status of the email.
	 * 
	 * @param read true to mark as read, false otherwise.
	 */
	public void setRead(boolean read) {
		isRead = read;
	}

	/**
	 * Gets the unique ID of the email.
	 * 
	 * @return The unique ID.
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets the unique ID of the email.
	 * 
	 * @param uniqueId The unique ID.
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Gets the list of attachment names.
	 * 
	 * @return List of attachment names.
	 */
	public List<String> getAttachmentNames() {
		return attachmentNames;
	}

	/**
	 * Sets the list of attachment names.
	 * 
	 * @param attachmentNames List of attachment names.
	 */
	public void setAttachmentNames(List<String> attachmentNames) {
		this.attachmentNames = attachmentNames;
	}

	/**
	 * Checks if the email has any attachments.
	 * 
	 * @return true if there are attachments, false otherwise.
	 */
	public boolean hasAttachments() {
		return attachmentNames != null && !attachmentNames.isEmpty();
	}

	/**
	 * Sets the content of the email.
	 * 
	 * @param content The content/body.
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
package metroMalaga.Clases;

import java.util.ArrayList;
import java.util.List;

public class EmailModel {
	private int messageNumber;
	private String sender;
	private String subject;
	private String content;
	private boolean isRead;
	private String uniqueId;
	// NUEVO: Lista de nombres de archivos adjuntos
	private List<String> attachmentNames;

	public EmailModel(int messageNumber, String sender, String subject, String content) {
		this.messageNumber = messageNumber;
		this.sender = sender;
		this.subject = subject;
		this.content = content;
		this.isRead = false;
		this.uniqueId = "";
		this.attachmentNames = new ArrayList<>();
	}

	// Getters y Setters
	public int getMessageNumber() {
		return messageNumber;
	}

	public String getSender() {
		return sender;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	// NUEVOS GETTER/SETTER PARA ADJUNTOS
	public List<String> getAttachmentNames() {
		return attachmentNames;
	}

	public void setAttachmentNames(List<String> attachmentNames) {
		this.attachmentNames = attachmentNames;
	}

	// Método auxiliar para saber si tiene adjuntos
	public boolean hasAttachments() {
		return attachmentNames != null && !attachmentNames.isEmpty();
	}
}
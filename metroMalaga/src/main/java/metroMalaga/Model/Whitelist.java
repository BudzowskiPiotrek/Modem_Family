package metroMalaga.Model;

/**
 * Represents an entry in the system's email whitelist.
 * This model is used to manage authorized email addresses that are 
 * permitted to register or access specific administrative features.
 */
public class Whitelist {

	private String email;

	/**
	 * Constructs a new Whitelist entry with the specified email address.
	 * @param email The authorized email string.
	 */
	public Whitelist(String email) {
		this.email = email;
	}

	/**
	 * Retrieves the whitelisted email address.
	 * @return The email address as a String.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Updates the whitelisted email address.
	 * @param email The new email address to be authorized.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
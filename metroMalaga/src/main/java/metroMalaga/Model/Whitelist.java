package metroMalaga.Model;

/**
 * Model class for whitelisted email addresses.
 */
public class Whitelist {

	private String email;

	/**
	 * Constructor for Whitelist.
	 * 
	 * @param email The allowed email address.
	 */
	public Whitelist(String email) {
		this.email = email;
	}

	/**
	 * Gets the allowed email address.
	 * 
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the allowed email address.
	 * 
	 * @param email The email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
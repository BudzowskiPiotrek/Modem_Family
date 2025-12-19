package metroMalaga.Model;

/**
 * Represents a system user within the application.
 * This class stores authentication credentials, contact information, 
 * and the specific role assigned to the user for access control.
 */
public class Usuario {
	private String usernameApp;
	private String passwordApp;
	private String emailReal;
	private Rol rol;

	/**
	 * Constructs a new Usuario with the specified credentials and role.
	 * * @param usernameApp The unique identifier for the user in the application.
	 * @param passwordApp The encrypted or plain-text password for authentication.
	 * @param emailReal    The verified email address of the user.
	 * @param rol          The Role object defining the user's permissions.
	 */
	public Usuario(String usernameApp, String passwordApp, String emailReal, Rol rol) {
		super();
		this.usernameApp = usernameApp;
		this.passwordApp = passwordApp;
		this.emailReal = emailReal;
		this.rol = rol;
	}

	/**
	 * Retrieves the application username.
	 * @return The current username.
	 */
	public String getUsernameApp() {
		return usernameApp;
	}

	/**
	 * Updates the application username.
	 * @param usernameApp The new username to be set.
	 */
	public void setUsernameApp(String usernameApp) {
		this.usernameApp = usernameApp;
	}

	/**
	 * Retrieves the user's password.
	 * @return The current password.
	 */
	public String getPasswordApp() {
		return passwordApp;
	}

	/**
	 * Updates the user's password.
	 * @param passwordApp The new password to be set.
	 */
	public void setPasswordApp(String passwordApp) {
		this.passwordApp = passwordApp;
	}

	/**
	 * Retrieves the verified email address.
	 * @return The email address as a String.
	 */
	public String getEmailReal() {
		return emailReal;
	}

	/**
	 * Updates the verified email address.
	 * @param emailReal The new email address to be set.
	 */
	public void setEmailReal(String emailReal) {
		this.emailReal = emailReal;
	}

	/**
	 * Retrieves the role assigned to this user.
	 * @return The Rol object.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Assigns a new role to this user.
	 * @param rol The new Rol to be assigned.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
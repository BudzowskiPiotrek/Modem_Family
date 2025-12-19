package metroMalaga.Model;

/**
 * Model class representing a user application.
 */
public class Usuario {
	private String usernameApp;
	private String passwordApp;
	private String emailReal;
	private Rol rol;

	/**
	 * Constructor for Usuario.
	 * 
	 * @param usernameApp The application username.
	 * @param passwordApp The application password.
	 * @param emailReal   The real email address of the user.
	 * @param rol         The user's role.
	 */
	public Usuario(String usernameApp, String passwordApp, String emailReal, Rol rol) {
		super();
		this.usernameApp = usernameApp;
		this.passwordApp = passwordApp;
		this.emailReal = emailReal;
		this.rol = rol;
	}

	/**
	 * Gets the application username.
	 * 
	 * @return The username.
	 */
	public String getUsernameApp() {
		return usernameApp;
	}

	/**
	 * Sets the application username.
	 * 
	 * @param usernameApp The username.
	 */
	public void setUsernameApp(String usernameApp) {
		this.usernameApp = usernameApp;
	}

	/**
	 * Gets the application password.
	 * 
	 * @return The password.
	 */
	public String getPasswordApp() {
		return passwordApp;
	}

	/**
	 * Sets the application password.
	 * 
	 * @param passwordApp The password.
	 */
	public void setPasswordApp(String passwordApp) {
		this.passwordApp = passwordApp;
	}

	/**
	 * Gets the real email address.
	 * 
	 * @return The email address.
	 */
	public String getEmailReal() {
		return emailReal;
	}

	/**
	 * Sets the real email address.
	 * 
	 * @param emailReal The email address.
	 */
	public void setEmailReal(String emailReal) {
		this.emailReal = emailReal;
	}

	/**
	 * Gets the user's role.
	 * 
	 * @return The role.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Sets the user's role.
	 * 
	 * @param rol The role.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
package metroMalaga.Model;

public class Usuario {
	private String usernameApp;
	private String passwordApp;
	private String emailReal;
	private Rol rol;

	public Usuario(String usernameApp, String passwordApp, String emailReal, Rol rol) {
		super();
		this.usernameApp = usernameApp;
		this.passwordApp = passwordApp;
		this.emailReal = emailReal;
		this.rol = rol;
	}

	public Usuario(String string, String string2, String string3, Object rol2) {
		// TODO Auto-generated constructor stub
	}

	public String getUsernameApp() {
		return usernameApp;
	}

	public void setUsernameApp(String usernameApp) {
		this.usernameApp = usernameApp;
	}

	public String getPasswordApp() {
		return passwordApp;
	}

	public void setPasswordApp(String passwordApp) {
		this.passwordApp = passwordApp;
	}

	public String getEmailReal() {
		return emailReal;
	}

	public void setEmailReal(String emailReal) {
		this.emailReal = emailReal;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
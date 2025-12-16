package metroMalaga.Model;

public class Usuario {
    private String usernameApp;   
    private String passwordApp;  
    private String emailReal;

    public Usuario(String usernameApp, String passwordApp, String emailReal) {
        this.usernameApp = usernameApp;
        this.passwordApp = passwordApp;
        this.emailReal = emailReal;
    }

    public String getUsernameApp() { return usernameApp; }
    public void setUsernameApp(String usernameApp) { this.usernameApp = usernameApp; }

    public String getPasswordApp() { return passwordApp; }
    public void setPasswordApp(String passwordApp) { this.passwordApp = passwordApp; }

    public String getEmailReal() { return emailReal; }
    public void setEmailReal(String emailReal) { this.emailReal = emailReal; }
}
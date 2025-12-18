package metroMalaga.Controller.login;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

public class LoginEnterKeyListener extends KeyAdapter {

    private final JButton loginButton;
    private final LoginAttempt loginAttempt;

    public LoginEnterKeyListener(JButton loginButton, LoginAttempt loginAttempt) {
        this.loginButton = loginButton;
        this.loginAttempt = loginAttempt;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loginAttempt.actionPerformed(new ActionEvent(loginButton, ActionEvent.ACTION_PERFORMED, null));
        }
    }
}

package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import metroMalaga.Model.Rol;
import metroMalaga.Model.Usuario;

public class ServiceLogin {

	public ConnecionSQL conSQL = new ConnecionSQL();

	public boolean authenticateUser(String usuario, String password) {

		final String SQL = "SELECT username FROM usuarios WHERE username = ? AND password = ?";
		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			String errorMessage = "Connection/SQL Error. Please contact support. Detail: " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "System Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public Usuario getUserData(String usuario) {
		Usuario user = null;
		final String SQL = "SELECT * FROM usuarios WHERE username = ?";
		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					user = new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), new Rol(0, "", ""));
				}
			}

		} catch (SQLException e) {
			String errorMessage = "Error retrieving user data:" + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
		}
		return user;
	}

	public void registerLog(String user, String description) {
		final String SQL = "INSERT INTO logs (username, accion) VALUES (?, ?)";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, user);
			ps.setString(2, description);
			ps.executeUpdate();
		} catch (SQLException e) {
			String errorMessage = "CRITICAL LOGGING ERROR: Could not register log entry. Detail: " + e.getMessage();
			System.err.println(errorMessage);
		}
	}
}

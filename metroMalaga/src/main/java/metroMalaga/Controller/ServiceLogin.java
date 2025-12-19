package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import metroMalaga.Model.Rol;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

/**
 * Service class for handling user authentication and data retrieval.
 */
public class ServiceLogin {

	/**
	 * Database connection instance.
	 */
	public ConnecionSQL conSQL = new ConnecionSQL();

	/**
	 * Authenticates a user by checking the username and verifying the password.
	 * 
	 * @param usuario  The username.
	 * @param password The plain text password.
	 * @return true if authentication is successful, false otherwise.
	 */
	public boolean authenticateUser(String usuario, String password) {

		// Modified to use BCrypt password verification
		// Instead of comparing passwords in SQL, we fetch the hash and verify it
		final String SQL = "SELECT password FROM usuarios WHERE username = ?";
		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String hashedPassword = rs.getString("password");
					// Verify the plain password against the BCrypt hash
					return PasswordUtil.verifyPassword(password, hashedPassword);
				}
				// User not found
				return false;
			}
		} catch (SQLException e) {
			String errorMessage = Language.get(184) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(185), JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Retrieves the full user data object, including role and permissions.
	 * 
	 * @param usuario  The username.
	 * @param password The plain text password (used to populate the user object).
	 * @return A Usuario object containing the user's data, or null if retrieval
	 *         fails.
	 */
	public Usuario getUserData(String usuario, String password) {
		Usuario user = null;

		final String SQL = "SELECT u.username, u.password, u.correo_electronico, u.fk_id_rol, r.nombre AS rol_nombre, r.permiso, "
				+ "r.can_download, r.can_modify, r.can_delete "
				+ "FROM usuarios u JOIN roles r ON u.fk_id_rol = r.id_roles WHERE u.username = ?";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {

					Rol rol = new Rol(
							rs.getInt("fk_id_rol"),
							rs.getString("permiso"),
							rs.getString("rol_nombre"),
							rs.getBoolean("can_download"),
							rs.getBoolean("can_modify"),
							rs.getBoolean("can_delete"));

					user = new Usuario(rs.getString("username"), password,
							rs.getString("correo_electronico"), rol);
				}
			}
		} catch (SQLException e) {
			String errorMessage = Language.get(186) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(187), JOptionPane.ERROR_MESSAGE);
		}
		return user;
	}

	/**
	 * Refresh user data from database (useful when role permissions change).
	 * 
	 * @param usuario Usuario object to refresh.
	 * @return Updated Usuario object with current database data.
	 */
	public Usuario refreshUserData(Usuario usuario) {
		if (usuario == null) {
			return null;
		}

		// Reload user data from database using username
		return getUserData(usuario.getUsernameApp(), usuario.getPasswordApp());
	}

}

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

public class ServiceLogin {

	public ConnecionSQL conSQL = new ConnecionSQL();

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

	public Usuario getUserData(String usuario, String password) {
		Usuario user = null;

		final String SQL = "SELECT u.username, u.password, u.correo_electronico, u.fk_id_rol, r.nombre AS rol_nombre, r.permiso "
				+ "FROM usuarios u JOIN roles r ON u.fk_id_rol = r.id_roles WHERE u.username = ?";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {

					Rol rol = new Rol(rs.getInt("fk_id_rol"), rs.getString("permiso"), rs.getString("rol_nombre"));

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

}

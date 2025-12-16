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
		final String SQL = "SELECT u.username, u.password, u.direccion_correo, "
				+ "r.id, r.nombre AS rol_nombre, r.permisos FROM usuarios u JOIN roles r ON u.id_rol = r.id "
				+ "WHERE u.username = ?";
		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, usuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Rol rol = new Rol(rs.getInt("id"), rs.getString("permisos"), rs.getString("rol_nombre"));

					user = new Usuario(rs.getString("username"), rs.getString("password"),
							rs.getString("direccion_correo"), rol);
				}
			}
		} catch (SQLException e) {
			String errorMessage = "Error retrieving user data:" + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
		}
		return user;
	}
}

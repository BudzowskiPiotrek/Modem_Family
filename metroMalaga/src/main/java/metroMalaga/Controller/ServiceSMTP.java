package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ServiceSMTP {

	public ConnecionSQL conSQL = new ConnecionSQL();

	public boolean isEmailInWhitelist(String email) {
		boolean exists = false;
		final String SQL = "SELECT COUNT(*) FROM lista_blanca WHERE correo_electronico = ?";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, email);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					exists = rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			String errorMessage = "Error verifying whitelist: " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
		}
		return exists;
	}
	public boolean isEmailInUsers(String email) {
		boolean exists = false;
		final String SQL = "SELECT COUNT(*) FROM usuarios WHERE correo_electronico = ?";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, email);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					exists = rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			String errorMessage = "Error verifying whitelist: " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
		}
		return exists;
	}
}
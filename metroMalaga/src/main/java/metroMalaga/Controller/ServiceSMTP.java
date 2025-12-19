package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import metroMalaga.Model.Language;

/**
 * Service class for SMTP-related database checks, such as whitelist and user
 * verification.
 */
public class ServiceSMTP {

	/**
	 * Database connection instance.
	 */
	public ConnecionSQL conSQL = new ConnecionSQL();

	/**
	 * Checks if an email address is in the whitelist.
	 * 
	 * @param email The email address to check.
	 * @return true if the email is in the whitelist, false otherwise.
	 */
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
			String errorMessage = Language.get(188) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(187), JOptionPane.ERROR_MESSAGE);
		}
		return exists;
	}

	/**
	 * Checks if an email address corresponds to a registered user.
	 * 
	 * @param email The email address to check.
	 * @return true if the email belongs to a registered user, false otherwise.
	 */
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
			String errorMessage = Language.get(188) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(187), JOptionPane.ERROR_MESSAGE);
		}
		return exists;
	}
}

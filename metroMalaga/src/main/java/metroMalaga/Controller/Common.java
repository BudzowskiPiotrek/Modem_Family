package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

public class Common {
	public ConnecionSQL conSQL = new ConnecionSQL();

	public static String formatSize(long bytes) {
		if (bytes <= 0) {
			return "0 B";
		}
		final long K = 1024;
		final long M = K * 1024;
		final long G = M * 1024;

		DecimalFormat df = new DecimalFormat("0.0");

		if (bytes < K) {
			return bytes + " B";
		} else if (bytes < M) {
			return df.format((double) bytes / K) + " KB";
		} else if (bytes < G) {
			return df.format((double) bytes / M) + " MB";
		} else {
			return df.format((double) bytes / G) + " GB";
		}
	}

	public void registerLog(String user, String description) {
		final String SQL = "INSERT INTO logs (username, accion) VALUES (?, ?)";

		try (Connection con = conSQL.connect(); PreparedStatement ps = con.prepareStatement(SQL)) {
			ps.setString(1, user);
			ps.setString(2, description);
			ps.executeUpdate();
		} catch (SQLException e) {
			String errorMessage = "CRITICAL LOGGING ERROR: Could not register log entry. Detail: " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "Critical Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

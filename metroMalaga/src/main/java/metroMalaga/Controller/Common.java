package metroMalaga.Controller;

import java.awt.Color;
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
	
	public static boolean isDarkMode = false;

    private static final Color L_BG_MAIN = new Color(245, 247, 250);
    private static final Color L_BG_PANEL = Color.WHITE;
    private static final Color L_TXT = new Color(50, 50, 50);
    private static final Color L_BORDER = new Color(220, 220, 220);
    
    private static final Color D_BG_MAIN = new Color(30, 30, 30);
    private static final Color D_BG_PANEL = new Color(45, 45, 48);
    private static final Color D_TXT = new Color(230, 230, 230);
    private static final Color D_BORDER = new Color(80, 80, 80);

    public static Color getBackground() { return isDarkMode ? D_BG_MAIN : L_BG_MAIN; }
    public static Color getPanelBackground() { return isDarkMode ? D_BG_PANEL : L_BG_PANEL; }
    public static Color getText() { return isDarkMode ? D_TXT : L_TXT; }
    public static Color getBorder() { return isDarkMode ? D_BORDER : L_BORDER; }
    public static Color getAccent() { return new Color(70, 130, 180); } 
    public static Color getDanger() { return new Color(220, 53, 69); } 
    
    public static Color getFieldBackground() { return isDarkMode ? new Color(60, 60, 60) : Color.WHITE; }

    public static Color getHoverColor(boolean isDanger, boolean isAccent) {
        if (isDanger) {
            return new Color(200, 40, 50);
        }
        if (isAccent) {
            return isDarkMode ? new Color(60, 100, 140) : new Color(235, 245, 255);
        }
        return isDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
    }
}
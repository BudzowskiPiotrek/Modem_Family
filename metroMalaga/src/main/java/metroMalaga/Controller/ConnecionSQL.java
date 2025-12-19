package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import metroMalaga.Model.Language;

/**
 * Utility class for establishing connections to the MySQL database.
 */
public class ConnecionSQL {

	private final String RUTA = "jdbc:mysql://localhost/centimetromalaga";
	private final String USUARIO = "root";
	private final String PASS = "";

	/**
	 * Establishes a connection to the database.
	 * 
	 * @return A Connection object, or null if connection fails.
	 */
	public Connection connect() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(RUTA, USUARIO, PASS);
		} catch (SQLException e) {
			String errorMessage = Language.get(195) + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, Language.get(196), JOptionPane.ERROR_MESSAGE);
		}
		return con;
	}
}

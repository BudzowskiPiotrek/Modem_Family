package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import metroMalaga.Model.Language;

public class ConnecionSQL {

	private final String RUTA = "jdbc:mysql://192.168.1.15/centimetromalaga";
	private final String USUARIO = "remoto";
	private final String PASS = "proyecto";

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

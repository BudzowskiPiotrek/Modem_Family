package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnecionSQL {

	private final String RUTA = "jdbc:mysql://localhost/proyectomultidisciplinar";
	private final String USUARIO = "root";
	private final String PASS = "";

	public Connection connect() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(RUTA, USUARIO, PASS);
		} catch (SQLException e) {
			String errorMessage = "CRITICAL DATABASE ERROR: Could not establish connection. Detail: " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage, "SQL Connection Error", JOptionPane.ERROR_MESSAGE);
		}
		return con;
	}
}

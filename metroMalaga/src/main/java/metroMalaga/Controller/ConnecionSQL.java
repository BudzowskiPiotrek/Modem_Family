package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnecionSQL {
	private final String RUTA = "jdbc:mysql://192.168.1.19:3306/centimetromalaga";
	private final String USUARIO = "remoto";
	private final String PASS = "proyecto";

	public Connection connect() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(RUTA, USUARIO, PASS);
		} catch (SQLException e) {
			
		}
		return con;
	}
}

package metroMalaga.frontend.ftp;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTable;

import metroMalaga.Clases.Usuario;

public class PanelFTP extends JFrame{

	private Usuario user;
	private File file;
	private JTable table;
	private ArrayList<String> columns;
	
	private final Color P5_RED = new Color(220, 20, 60);
	private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
	private final Color P5_BLACK = new Color(20, 20, 20);
	private final Color P5_WHITE = new Color(240, 240, 240);

	// Usamos la fuente de botones definida en el Login
	private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 24);
	public PanelFTP(Usuario user) {
		this.user = user;
		this.columns=new ArrayList<>();
		
		propertiesWindow();
		setTitle();
		addColumns();
		//createTable();
		this.setVisible(true);
	}
//	private void createTable() {
//		// TODO Auto-generated method stub
//		JTable table = new JTable(data,columns);
//	}
	private void propertiesWindow() {
		// Usamos GridBagLayout para centrar los elementos como en el Login
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(P5_RED); // Fondo Rojo P5

		this.setSize(700, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	private void setTitle() {
		// Título de la ventana
		this.setTitle("FTP Window");
	}
	private void addColumns() {
		columns.add("Date");
		columns.add("Name");
		columns.add("Buttons");
	}

}

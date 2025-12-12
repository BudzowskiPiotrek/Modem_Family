package metroMalaga.frontend.ftp;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Clases.FTPTableModel;
import metroMalaga.Clases.Usuario;
import metroMalaga.backend.HandleFTPlist;
import metroMalaga.backend.ServiceFTP;

public class PanelFTP extends JFrame {
public static void main(String[] args) {
	PanelFTP p = new PanelFTP(new Usuario());
}
	private Usuario user;
	private FTPTableModel ftpModel;
	private JTable fileTable;
	private JTextField searchField;
	private JButton uploadButton;

	public PanelFTP(Usuario user) {
		ServiceFTP service = new ServiceFTP("readwrite"); // + user.getNombre()
		FTPFile[] fileArray = service.listAllFiles();
		List<FTPFile> initialFiles = new ArrayList<>(Arrays.asList(fileArray));
		this.ftpModel = new FTPTableModel(initialFiles);
	
		initializeComponents();
		attachListeners();
		setupFrameConfiguration();
		setupLayout();

	}
	private void initializeComponents() {
        this.fileTable = new JTable(this.ftpModel);
        this.searchField = new JTextField(20);
        this.uploadButton = new JButton("+");
    }
	private void attachListeners() {
		HandleFTPlist listener = new HandleFTPlist(searchField, ftpModel);
	}
	private void setupFrameConfiguration() {
        this.setTitle("FTP Manager - "); // + user.getNombre()
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        this.setLayout(new BorderLayout()); 
        this.setSize(800, 600); 
        this.setLocationRelativeTo(null); 
    }
    
    private void setupLayout() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Filtrar:")); 
        bottomPanel.add(this.searchField);
        this.add(new JScrollPane(fileTable), BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

}

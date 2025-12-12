package metroMalaga.frontend.ftp;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class ButtonsPanel extends JPanel {
	public final JButton downloadButton = new JButton("Descargar");
	public final JButton deleteButton = new JButton("Borrar");
	public final JButton renameButton = new JButton("Modificar");

	public ButtonsPanel() {
		this.setLayout(new GridLayout(1, 3, 5, 0));
		this.add(downloadButton);
		this.add(deleteButton);
		this.add(renameButton);
	}
}

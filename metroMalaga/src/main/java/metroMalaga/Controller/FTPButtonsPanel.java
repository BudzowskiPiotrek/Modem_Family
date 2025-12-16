package metroMalaga.Controller;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class FTPButtonsPanel extends JPanel {
	public final JButton downloadButton = new JButton("📥");
	public final JButton deleteButton = new JButton("❌");
	public final JButton renameButton = new JButton("🖊️");

	public FTPButtonsPanel() {
		this.setLayout(new GridLayout(1, 3, 5, 0));
		this.add(downloadButton);
		this.add(deleteButton);
		this.add(renameButton);
	}
}

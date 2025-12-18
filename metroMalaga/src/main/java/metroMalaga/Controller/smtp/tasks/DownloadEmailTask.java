package metroMalaga.Controller.smtp.tasks;

import java.awt.Component;
import java.io.File;
import javax.swing.JOptionPane;
import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.Language;

public class DownloadEmailTask implements Runnable {

	private final HandleSMTP backend;
	private final Component parentView;
	private final String uid;
	private final File dest;

	public DownloadEmailTask(HandleSMTP backend, Component parentView, String uid, File dest) {
		this.backend = backend;
		this.parentView = parentView;
		this.uid = uid;
		this.dest = dest;
	}

	@Override
	public void run() {
		try {
			backend.downloadEmailComplete(uid, dest);
			JOptionPane.showMessageDialog(parentView, Language.get(163) + dest.getAbsolutePath());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentView, Language.get(164) + e.getMessage());
		}
	}
}

package metroMalaga.View;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * A non-modal loading dialog displayed during asynchronous FTP connection
 * tasks. It provides visual feedback to the user through an indeterminate
 * progress bar and status messages.
 */
public class LoadingDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;
	private JProgressBar progressBar;

	/**
	 * Constructs a new LoadingDialog and initializes its graphical components.
	 */
	public LoadingDialog() {
		initComponents();
	}

	/**
	 * Initializes the dialog's properties and layout components.
	 */
	private void initComponents() {
		setTitle("Conectando a FTP");
		setModal(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setSize(350, 150);
		setLocationRelativeTo(null);
		setUndecorated(false);

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		statusLabel = new JLabel("Conectando al servidor FTP...", SwingConstants.CENTER);

		panel.add(statusLabel, BorderLayout.NORTH);
		panel.add(progressBar, BorderLayout.CENTER);

		add(panel);
	}

	/**
	 * Updates the status message displayed in the dialog's label. * @param status
	 * The new status text to be shown to the user.
	 */
	public void updateStatus(String status) {
		if (statusLabel != null) {
			statusLabel.setText(status);
		}
	}

	/**
	 * Makes the loading dialog visible on the screen.
	 */
	public void showDialog() {
		setVisible(true);
	}

	/**
	 * Disposes of the dialog and releases its screen resources.
	 */
	public void closeDialog() {
		dispose();
	}
}
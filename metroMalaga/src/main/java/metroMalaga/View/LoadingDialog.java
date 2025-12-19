package metroMalaga.View;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Loading dialog shown while connecting to FTP server.
 */
public class LoadingDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    /**
     * Constructor for LoadingDialog.
     */
    public LoadingDialog() {
        initComponents();
    }

    /**
     * Initializes the components of the dialog.
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

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        // Status label
        statusLabel = new JLabel("Conectando al servidor FTP...", SwingConstants.CENTER);

        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        panel.add(panel);
    }

    /**
     * Update the status message shown in the dialog.
     * 
     * @param status New status message.
     */
    public void updateStatus(String status) {
        if (statusLabel != null) {
            statusLabel.setText(status);
        }
    }

    /**
     * Show the loading dialog.
     */
    public void showDialog() {
        setVisible(true);
    }

    /**
     * Close the loading dialog.
     */
    public void closeDialog() {
        dispose();
    }
}

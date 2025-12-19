package metroMalaga.Controller.ftp;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.net.ftp.FTPFile;
import metroMalaga.Model.Usuario;

public class FTPButtonsRenderer extends FTPButtonsPanel implements TableCellRenderer {

	private Usuario user;

	/**
	 * Constructor for FTPButtonsRenderer.
	 * 
	 * @param user The current user.
	 */
	public FTPButtonsRenderer(Usuario user) {
		this.user = user;
	}

	/**
	 * Configures the renderer component for the table cell.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (isSelected) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}

		// Check if user can modify/delete this file
		if (value instanceof FTPFile) {
			FTPFile file = (FTPFile) value;
			String filename = file.getName();

			boolean canDelete = metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(
					filename,
					user.getUsernameApp(),
					user.getRol(),
					true // checking delete permission
			);

			boolean canModify = metroMalaga.Model.FTPFileOwnershipDAO.canModifyFile(
					filename,
					user.getUsernameApp(),
					user.getRol(),
					false // checking modify/rename permission
			);

			boolean canDownload = metroMalaga.Model.FTPFileOwnershipDAO.canDownloadFile(user.getRol());

			// Show/hide buttons based on permissions
			deleteButton.setVisible(canDelete);
			renameButton.setVisible(canModify);
			downloadButton.setVisible(canDownload);
		}

		return this;
	}
}

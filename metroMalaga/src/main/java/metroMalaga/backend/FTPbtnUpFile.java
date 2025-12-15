package metroMalaga.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Clases.FTPTableModel;

public class FTPbtnUpFile implements ActionListener {

	private ServiceFTP service;
	private FTPTableModel model;

	public FTPbtnUpFile(JButton button, ServiceFTP service, FTPTableModel model) {
		this.service = service;
		this.model = model;
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccionar archivo para subir");

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			File localFile = fileChooser.getSelectedFile();

			String remoteFileName = localFile.getName();

			try {
				boolean success = service.uploadFile(localFile.getAbsolutePath(), remoteFileName);

				if (success) {
					JOptionPane.showMessageDialog(null, "Archivo subido con éxito: " + remoteFileName);

					FTPFile[] updatedFilesArray = service.listAllFiles();

					// 2. Convertir el Array a una Lista
					List<FTPFile> updatedFilesList = new ArrayList<>(Arrays.asList(updatedFilesArray));

					// 3. Actualizar el modelo con la Lista
					model.setData(updatedFilesList);

				} else {
					JOptionPane.showMessageDialog(null, "Fallo al subir el archivo.", "Error de Subida",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error de E/S durante la subida: " + ex.getMessage(),
						"Error de Subida", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

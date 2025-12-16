package metroMalaga.Controller.ftp;

import metroMalaga.Model.FTPTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;

public class FTPRefreshThread extends Thread {
    private final ServiceFTP service;
    private final FTPTableModel ftpModel;
    private final long refreshInterval = 5000;
    private volatile boolean running = true;

    public FTPRefreshThread(ServiceFTP service, FTPTableModel ftpModel) {
        this.service = service;
        this.ftpModel = ftpModel;
        setDaemon(true); 
    }

    public void stopRunning() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(refreshInterval);
                if (running) {
                	FTPFile[] fileArray = service.listAllFiles();
        			List<FTPFile> newFiles = new ArrayList<>(Arrays.asList(fileArray));
        			ftpModel.setData(newFiles);
                    System.out.println("FTP: Archivos actualizados automáticamente.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            } catch (Exception e) {
                System.err.println("Error en hilo de actualización FTP: " + e.getMessage());
            }
        }
    }
}
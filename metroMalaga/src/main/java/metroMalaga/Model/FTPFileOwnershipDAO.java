package metroMalaga.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import metroMalaga.Controller.ConnecionSQL;

/**
 * DAO for managing FTP file ownership in database.
 */
public class FTPFileOwnershipDAO {

    /**
     * Register a file upload in the database.
     * 
     * @param filename Name of the file.
     * @param username Username who uploaded the file.
     * @param isFolder True if it's a folder, false if it's a file.
     */
    public static void registerFile(String filename, String username, boolean isFolder) {
        String sql = "INSERT INTO ftp_file_ownership (filename, uploaded_by, file_type) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE uploaded_by = ?, upload_date = CURRENT_TIMESTAMP";

        System.out.println("DEBUG DAO registerFile - filename: " + filename);
        System.out.println("DEBUG DAO registerFile - username: " + username);
        System.out.println("DEBUG DAO registerFile - isFolder: " + isFolder);

        try (Connection conn = new ConnecionSQL().connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("DEBUG DAO registerFile - Connection obtained: " + (conn != null));

            String fileType = isFolder ? "FOLDER" : "FILE";
            stmt.setString(1, filename);
            stmt.setString(2, username);
            stmt.setString(3, fileType);
            stmt.setString(4, username);

            int rows = stmt.executeUpdate();
            System.out.println("DEBUG DAO registerFile - Rows affected: " + rows);

        } catch (SQLException e) {
            System.err.println("ERROR registerFile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the owner of a file.
     * 
     * @param filename Name of the file.
     * @return Username of the owner, or null if file not found.
     */
    public static String getFileOwner(String filename) {
        String sql = "SELECT uploaded_by FROM ftp_file_ownership WHERE filename = ?";

        try (Connection conn = new ConnecionSQL().connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filename);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("uploaded_by");
            }

        } catch (SQLException e) {
            System.err.println("Error getting file owner: " + e.getMessage());
        }

        return null;
    }

    /**
     * Delete a file record from the database.
     * 
     * @param filename Name of the file to delete.
     */
    public static void deleteFile(String filename) {
        String sql = "DELETE FROM ftp_file_ownership WHERE filename = ?";

        try (Connection conn = new ConnecionSQL().connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filename);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting file ownership record: " + e.getMessage());
        }
    }

    /**
     * Rename a file in the database.
     * 
     * @param oldName Old filename.
     * @param newName New filename.
     */
    public static void renameFile(String oldName, String newName) {
        String sql = "UPDATE ftp_file_ownership SET filename = ? WHERE filename = ?";

        try (Connection conn = new ConnecionSQL().connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setString(2, oldName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error renaming file in ownership records: " + e.getMessage());
        }
    }

    /**
     * Check if a user can modify a file (delete or rename).
     * 
     * @param filename Name of the file.
     * @param username Username trying to modify.
     * @param rol      Rol object with permissions.
     * @param isDelete true if checking delete permission, false for modify/rename.
     * @return true if user can modify, false otherwise.
     */
    public static boolean canModifyFile(String filename, String username, Rol rol, boolean isDelete) {
        // Admin can modify anything
        if ("admin".equalsIgnoreCase(rol.getNombre())) {
            return true;
        }

        // Get file owner
        String owner = getFileOwner(filename);

        // If user is the owner, they can always modify their own files
        if (owner != null && owner.equalsIgnoreCase(username)) {
            return true;
        }

        // If not owner, check role permissions
        if (isDelete) {
            return rol.isCanDelete(); // Can delete files of others if role permits
        } else {
            return rol.isCanModify(); // Can modify/rename files of others if role permits
        }
    }

    /**
     * Check if a user can download a file.
     * 
     * @param rol Rol object with permissions.
     * @return true if user can download, false otherwise.
     */
    public static boolean canDownloadFile(Rol rol) {
        // Admin can always download
        if ("admin".equalsIgnoreCase(rol.getNombre())) {
            return true;
        }

        // Check role permission
        return rol.isCanDownload();
    }
}

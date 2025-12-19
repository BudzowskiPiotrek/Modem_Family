package metroMalaga.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Utility program to migrate existing plain text passwords to BCrypt hashes.
 * 
 * USAGE:
 * 1. First, run the SQL script to modify the password column size
 * 2. Run this program to hash all existing passwords
 * 3. Verify that login still works with the old passwords
 * 
 * WARNING: Make a backup of your database before running this!
 */
public class PasswordMigrationTool {

    private static ConnecionSQL conSQL = new ConnecionSQL();

    /**
     * Main method to execute the migration tool.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("=== Password Migration Tool ===");
        System.out.println("This tool will migrate all plain text passwords to BCrypt hashes.");
        System.out.println();

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "WARNING: This will modify all passwords in the database.\n" +
                        "Make sure you have a backup!\n\n" +
                        "Have you already executed the SQL script to modify the password column?\n" +
                        "(ALTER TABLE usuarios MODIFY COLUMN password VARCHAR(255))",
                "Confirm Migration",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            System.out.println("Migration cancelled by user.");
            return;
        }

        try {
            migratePasswords();
            JOptionPane.showMessageDialog(null,
                    "Migration completed successfully!\n" +
                            "Check the console for details.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Migration failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Performs the password migration process.
     * 
     * @throws SQLException If a database error occurs.
     */
    private static void migratePasswords() throws SQLException {
        List<UserPassword> users = fetchAllUsers();

        System.out.println("Found " + users.size() + " users to migrate.");
        System.out.println();

        int migrated = 0;
        int skipped = 0;

        for (UserPassword user : users) {
            // Check if password is already a BCrypt hash
            if (PasswordUtil.isBCryptHash(user.password)) {
                System.out.println("SKIP: User '" + user.username + "' - already hashed");
                skipped++;
                continue;
            }

            // Hash the plain password
            String hashedPassword = PasswordUtil.hashPassword(user.password);

            // Update in database
            updatePassword(user.username, hashedPassword);

            System.out.println("MIGRATED: User '" + user.username + "'");
            System.out.println("  Plain: " + user.password);
            System.out.println("  Hash:  " + hashedPassword);
            System.out.println();

            migrated++;
        }

        System.out.println("=== Migration Summary ===");
        System.out.println("Total users: " + users.size());
        System.out.println("Migrated: " + migrated);
        System.out.println("Skipped (already hashed): " + skipped);
    }

    /**
     * Fetches all users and their passwords from the database.
     * 
     * @return A list of UserPassword objects.
     * @throws SQLException If a database error occurs.
     */
    private static List<UserPassword> fetchAllUsers() throws SQLException {
        List<UserPassword> users = new ArrayList<>();

        final String SQL = "SELECT username, password FROM usuarios";

        try (Connection con = conSQL.connect();
                PreparedStatement ps = con.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserPassword user = new UserPassword();
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                users.add(user);
            }
        }

        return users;
    }

    /**
     * Updates the password for a specific user in the database.
     * 
     * @param username       The username.
     * @param hashedPassword The new hashed password.
     * @throws SQLException If a database error occurs.
     */
    private static void updatePassword(String username, String hashedPassword) throws SQLException {
        final String SQL = "UPDATE usuarios SET password = ? WHERE username = ?";

        try (Connection con = conSQL.connect();
                PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setString(1, hashedPassword);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    /**
     * Simple inner class to hold user credentials.
     */
    private static class UserPassword {
        String username;
        String password;
    }
}
